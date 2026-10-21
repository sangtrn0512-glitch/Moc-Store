"""Local integration test. Uses only test_* accounts and a local SMTP sink, never Gmail.
Run the app on port 18085 with SMTP 127.0.0.1:10255, auth/STARTTLS disabled.
"""
from pathlib import Path
import http.cookiejar, urllib.request, urllib.parse, urllib.error, re, subprocess
import socketserver, threading, email, email.policy
BASE="http://localhost:18085"
PREFIX="test_asm_full_"
MAIL=[]
UPLOADS=[]
class SMTP(socketserver.StreamRequestHandler):
    def handle(self):
        self.wfile.write(b"220 localhost test SMTP\r\n")
        while True:
            line=self.rfile.readline()
            if not line:break
            cmd=line.decode(errors="replace").upper()
            if cmd.startswith(("EHLO","HELO")):self.wfile.write(b"250 localhost\r\n")
            elif cmd.startswith("DATA"):
                self.wfile.write(b"354 end with dot\r\n");data=b""
                while True:
                    part=self.rfile.readline()
                    if part==b".\r\n" or not part:break
                    data+=part
                MAIL.append(data);self.wfile.write(b"250 OK\r\n")
            elif cmd.startswith("QUIT"):self.wfile.write(b"221 bye\r\n");break
            else:self.wfile.write(b"250 OK\r\n")
class Server(socketserver.ThreadingTCPServer):allow_reuse_address=True
smtp=Server(("127.0.0.1",10255),SMTP);threading.Thread(target=smtp.serve_forever,daemon=True).start()
def sql(query):
    return subprocess.check_output(["sqlcmd","-S","localhost","-E","-C","-d","ASMJava5","-b","-h","-1","-W","-Q","SET NOCOUNT ON; "+query],text=True).strip()
def cleanup():
    sql("DELETE d FROM order_details d JOIN orders o ON o.id=d.order_id WHERE o.username LIKE 'test[_]asm[_]full[_]%'; DELETE FROM orders WHERE username LIKE 'test[_]asm[_]full[_]%'; DELETE FROM accounts WHERE username LIKE 'test[_]asm[_]full[_]%'; DELETE FROM products WHERE name LIKE 'test[_]asm[_]full[_]%'; DELETE FROM categories WHERE name LIKE 'test[_]asm[_]full[_]%';")
class Client:
    def __init__(self):self.op=urllib.request.build_opener(urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()));self.csrf=None
    def req(self,path,data=None,expected=200):
        if data is not None:data=dict(data,csrf=self.csrf or "")
        try:r=self.op.open(BASE+path,None if data is None else urllib.parse.urlencode(data).encode());body=r.read().decode();status=r.status
        except urllib.error.HTTPError as e:body=e.read().decode();status=e.code
        if status!=expected:
            Path("tmp-smoke-error.txt").write_text(body,encoding="utf-8")
            raise AssertionError((path,status,expected,"see tmp-smoke-error.txt"))
        match=re.search(r'name="csrf"[^>]*value="([^"]+)"',body)
        if match:self.csrf=match.group(1)
        return body
    def get(self,p,**kw):return self.req(p,**kw)
    def post(self,p,d,**kw):return self.req(p,d,**kw)
def register(c,suffix):
    user=PREFIX+suffix;c.get('/account/sign-up')
    s=c.post('/account/sign-up',dict(username=user,fullName='Test Customer',email=user+'@example.invalid',password='TestPassword123!'))
    assert 'kiểm tra email' in s,s[:200]
    token=sql("SELECT activation_token FROM accounts WHERE username='"+user+"'")
    assert token and token!='NULL'
    s=c.get('/account/activate?token='+token);assert 'Kích hoạt thành công' in s
    assert 'không hợp lệ' in c.get('/account/activate?token='+token)
    return user
def login(c,user,password='TestPassword123!'):
    c.get('/auth/login');return c.post('/auth/login',dict(username=user,password=password))
try:
    cleanup()
    a=Client();b=Client();guest=Client()
    assert 'Đăng nhập' in guest.get('/admin/product/index')
    assert 'Đăng nhập' in guest.get('/order/checkout')
    # Invalid CSRF rejected before mutation.
    guest.csrf='wrong';guest.post('/cart/clear',{},expected=403)
    u=register(a,'customer');v=register(b,'admin')
    assert len(MAIL)==2
    assert 'Sai tên' in login(a,u,'wrong')
    login(a,u)
    a.get('/admin/product/index',expected=403)
    sql("UPDATE accounts SET admin=1 WHERE username='"+v+"'")
    login(b,v)
    for p in ['/','/product/list','/product/list?page=1&sort=price','/product/list?minPrice=100000&maxPrice=500000','/account/edit-profile','/account/change-password','/order/list','/order/my-product-list']:
        a.get(p)
    for p in ['/admin/category/index','/admin/product/index','/admin/account/index','/admin/order/index','/admin/report/revenue','/admin/report/vip']:
        b.get(p)
    pid=sql("SELECT TOP 1 id FROM products WHERE available=1 ORDER BY id")
    a.post('/cart/add/'+pid,{})
    s=a.get('/order/checkout');key=re.search(r'name="checkoutKey"[^>]*value="([^"]+)"',s).group(1)
    s=a.post('/order/checkout',dict(address='Test address',phone='0901234567',checkoutKey=key));assert 'Đặt hàng thành công' in s
    oid=sql("SELECT TOP 1 id FROM orders WHERE username='"+u+"' ORDER BY id DESC")
    assert int(sql("SELECT COUNT(*) FROM order_details WHERE order_id="+oid))==1
    # A second customer cannot view another customer's order, even when admin on customer route.
    b.get('/order/detail/'+oid,expected=403)
    s=b.get('/admin/order/edit/'+oid);assert 'Test address' in s
    b.post('/admin/order/item',dict(orderId=oid,productId=pid,quantity=3))
    b.post('/admin/order/update',dict(id=oid,address='Updated test address',phone='0901234567',status='COMPLETED'))
    assert 'COMPLETED' in a.get('/order/detail/'+oid)
    assert 'Test Customer' in b.get('/admin/report/vip')
    assert 'Chưa có dữ liệu' not in b.get('/admin/report/revenue')
    assert 'Chưa có sản phẩm trong đơn' not in a.get('/order/my-product-list')
    # Profile edits and password reset route.
    assert 'Đã lưu hồ sơ' in a.post('/account/edit-profile',dict(fullName='Updated Test',email=u+'@example.invalid'))
    guest.get('/account/forgot-password');guest.post('/account/forgot-password',dict(email=u+'@example.invalid'))
    token=sql("SELECT reset_token FROM accounts WHERE username='"+u+"'")
    assert token!='NULL'
    assert 'Đã đổi mật khẩu' in guest.post('/account/reset-password',dict(token=token,password='NewPassword123!',confirmPassword='NewPassword123!'))
    assert 'Đăng nhập' in a.get('/order/list')
    assert 'không hợp lệ' in guest.post('/account/reset-password',dict(token=token,password='OtherPassword123!',confirmPassword='OtherPassword123!'))
    login(a,u,'NewPassword123!')
    # CRUD through admin, preserving the accounts/products that existed before the test.
    result=b.post('/admin/account/create',dict(username=PREFIX+'managed',fullName='Managed',email=PREFIX+'managed@example.invalid',password='ManagedPassword123!',admin='false',activated='true'))
    assert 'Đã lưu tài khoản' in result
    assert 'Managed' in b.get('/admin/account/index?keyword='+PREFIX+'managed')
    b.post('/admin/account/update',dict(username=PREFIX+'managed',fullName='Managed Edited',email=PREFIX+'managed@example.invalid',password='',admin='false',activated='true'))
    assert 'Managed Edited' in b.get('/admin/account/edit/'+PREFIX+'managed')
    b.post('/admin/account/delete/'+PREFIX+'managed',{})
    assert sql("SELECT COUNT(*) FROM accounts WHERE username='"+PREFIX+"managed'")=='0'
    b.post('/admin/category/create',dict(name=PREFIX+'category'))
    cid=sql("SELECT id FROM categories WHERE name='"+PREFIX+"category'")
    b.post('/admin/category/update',dict(id=cid,name=PREFIX+'category_edited'))
    b.post('/admin/product/create',dict(name=PREFIX+'product',price='120000',discount='10',categoryId=cid,image='vase.svg',available='true'))
    testpid=sql("SELECT id FROM products WHERE name='"+PREFIX+"product'")
    assert PREFIX+'product' in b.get('/admin/product/edit/'+testpid)
    b.post('/admin/product/update',dict(id=testpid,name=PREFIX+'product_edited',price='150000',discount='5',categoryId=cid,image='vase.svg',available='true'))
    assert '150000' in b.get('/admin/product/edit/'+testpid)
    # Roll back the whole checkout when one of two products stops selling.
    c=Client();login(c,u,'NewPassword123!')
    c.post('/cart/add/'+pid,{});c.post('/cart/add/'+testpid,{})
    sql('UPDATE products SET available=0 WHERE id='+testpid)
    before=sql("SELECT COUNT(*) FROM orders WHERE username='"+u+"'")
    html=c.get('/order/checkout');key=re.search(r'name="checkoutKey"[^>]*value="([^"]+)"',html).group(1)
    result=c.post('/order/checkout',dict(address='Rollback test',phone='0901234567',checkoutKey=key))
    assert 'không còn bán' in result
    assert before==sql("SELECT COUNT(*) FROM orders WHERE username='"+u+"'")
    assert 'không thể' in b.post('/admin/category/delete/'+cid,{}).lower()
    b.post('/admin/product/delete/'+testpid,{})
    b.post('/admin/category/delete/'+cid,{})
    assert sql('SELECT COUNT(*) FROM categories WHERE id='+cid)=='0'
    # Multipart upload: a small PNG is accepted and served; a text file is rejected.
    import struct,zlib,uuid
    def chunk(kind,data):return struct.pack('!I',len(data))+kind+data+struct.pack('!I',zlib.crc32(kind+data)&0xffffffff)
    png=b'\x89PNG\r\n\x1a\n'+chunk(b'IHDR',struct.pack('!2I5B',1,1,8,2,0,0,0))+chunk(b'IDAT',zlib.compress(b'\x00\x00\x80\x00'))+chunk(b'IEND',b'')
    def upload(c,content):
        boundary='test'+uuid.uuid4().hex
        body=('--'+boundary+'\r\nContent-Disposition: form-data; name="csrf"\r\n\r\n'+c.csrf+'\r\n--'+boundary+'\r\nContent-Disposition: form-data; name="photo"; filename="test.png"\r\nContent-Type: image/png\r\n\r\n').encode()+content+('\r\n--'+boundary+'--\r\n').encode()
        req=urllib.request.Request(BASE+'/account/photo',data=body,headers={'Content-Type':'multipart/form-data; boundary='+boundary})
        return c.op.open(req).read().decode()
    c.get('/account/edit-profile');assert 'Đã cập nhật ảnh' in upload(c,png)
    photoname=sql("SELECT photo FROM accounts WHERE username='"+u+"'")
    UPLOADS.append(Path('uploads',photoname))
    assert c.op.open(BASE+'/uploads/'+photoname).read().startswith(b'\x89PNG')
    assert 'Ảnh không hợp lệ' in upload(c,b'not an image')
    Path('uploads',photoname).unlink()
    # Expired activation must fail; reissue generates a fresh token.
    expired=PREFIX+'expired';guest.get('/account/sign-up')
    guest.post('/account/sign-up',dict(username=expired,fullName='Expired Test',email=expired+'@example.invalid',password='TestPassword123!'))
    token=sql("SELECT activation_token FROM accounts WHERE username='"+expired+"'")
    sql("UPDATE accounts SET activation_expires_at=DATEADD(HOUR,-1,GETDATE()) WHERE username='"+expired+"'")
    assert 'hết hạn' in guest.get('/account/activate?token='+token)
    guest.post('/account/resend-activation',dict(email=expired+'@example.invalid'))
    newtoken=sql("SELECT activation_token FROM accounts WHERE username='"+expired+"'")
    assert token!=newtoken
    assert 'thành công' in guest.get('/account/activate?token='+newtoken)
    # Admin order create and delete.
    b.post('/admin/order/create',dict(username=u,productId=pid,quantity=2,address='Admin test',phone='0901234567'))
    temporder=sql("SELECT TOP 1 id FROM orders WHERE username='"+u+"' ORDER BY id DESC")
    b.post('/admin/order/delete/'+temporder,{})
    assert sql('SELECT COUNT(*) FROM orders WHERE id='+temporder)=='0'
    # Search, paging, and homepage still render after completed sales exist.
    for path in ['/','/product/list?keyword=M%E1%BB%99c&sort=priceDesc&page=1', '/admin/product/index?page=1&sort=price','/admin/account/index?keyword=test','/admin/category/index?sort=name']:
        b.get(path)
    a.post('/auth/logout',{});assert 'Đăng nhập' in a.get('/order/list')
    print('PASS: CRUD, rollback, registration/activation, local email, login/logout, CSRF, admin/customer permissions, checkout, ownership, order editing, reports, profile, password reset and session revocation')
finally:
    cleanup();smtp.shutdown();smtp.server_close()
    for file in UPLOADS:
        if file.is_file():file.unlink()
