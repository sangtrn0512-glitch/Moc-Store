"""Build traceable Y3/Y4 documents from real Surefire XML, never invent test outcomes."""
from pathlib import Path
from collections import Counter, defaultdict
from datetime import datetime, timezone, timedelta
import argparse
import hashlib
import html
import json
import re
import shutil
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'docs/testing'
parser = argparse.ArgumentParser()
parser.add_argument('--reports', nargs='+', default=['target/surefire-reports', 'target/selenium-reports'])
args = parser.parse_args()
OUT.mkdir(parents=True, exist_ok=True)
catalogue = json.loads((OUT / 'test-case-catalogue.json').read_text(encoding='utf-8'))
sources = {}
mapping = {}
for path in (ROOT / 'src/test/java').rglob('*.java'):
    text = path.read_text(encoding='utf-8')
    package = re.search(r'package\s+([\w.]+);', text).group(1)
    classname = package + '.' + path.stem
    sources[classname] = path
    pattern = r'@CaseIds\((.*?)\)\s*(?:@\w+(?:\([^)]*\))?\s*)*(?:public\s+)?void\s+(\w+)\s*\('
    for match in re.finditer(pattern, text, re.S):
        mapping[(classname, match.group(2))] = re.findall(r'TC-\d+', match.group(1))

results = []
seen = set()
for folder in args.reports:
    reportdir = ROOT / folder
    for path in sorted(reportdir.glob('TEST-*.xml')):
        suite = ET.parse(path).getroot()
        evidence = OUT / 'evidence' / reportdir.name
        evidence.mkdir(parents=True, exist_ok=True)
        shutil.copy2(path, evidence / path.name)
        for test in suite.findall('testcase'):
            classname, name = test.get('classname'), test.get('name')
            key = (classname, name)
            if key in seen:
                raise ValueError(f'Duplicate execution in input report sets: {key}')
            seen.add(key)
            method = re.split(r'[\[(]', name)[0]
            failure = test.find('failure')
            if failure is None:
                failure = test.find('error')
            status = 'Fail' if failure is not None else ('Skipped' if test.find('skipped') is not None else 'Pass')
            results.append(dict(classname=classname, method=method, name=name, status=status,
                ids=mapping.get((classname, method), []), seconds=test.get('time'),
                error=(failure.get('message', '') if failure is not None else ''),
                evidence=str((evidence / path.name).relative_to(OUT)).replace('\\', '/'),
                timestamp=suite.get('timestamp', 'not recorded')))

if not results:
    raise SystemExit('No XML reports found. Run Maven tests first.')
by_case = defaultdict(list)
for test in results:
    for caseid in test['ids']:
        by_case[caseid].append(test)
unknown = set(by_case) - {c['id'] for c in catalogue}
assert not unknown, unknown
timestamp = datetime.now(timezone(timedelta(hours=7))).isoformat(timespec='seconds')
counts = Counter(t['status'] for t in results)
manual = {
    'TC-118': 'Not Run: không kết nối SQL Server thật để đếm dữ liệu mẫu; H2 chỉ chứa fixture kiểm thử.',
    'TC-119': 'Not Run: script Database/database.sql hiện có; chưa thực thi cài mới trên SQL Server. H2 tự sinh schema không chứng minh script SQL Server chạy được.',
    'TC-121': 'Not Run: chưa kiểm tra thủ công toàn bộ giao diện ở cả desktop và mobile; Selenium hiện dùng 1440x1000.',
}
notes = {
    'TC-044': 'Controller dùng mock kho file; FileService thật được kiểm tra bằng JVM con trong thư mục tạm riêng, không ghi vào uploads của ứng dụng chính.',
    'TC-085': 'Controller dùng mock kho file; FileService thật có test độc lập trong thư mục tạm. Không còn lỗi imageFile ở source hiện tại.',
    'TC-074': 'Source hiện tại chặn tên quá 100 tại CategoryService.save; test xác nhận HTTP400 và không thêm bản ghi. Khoảng trống validation của snapshot cũ không tái hiện.',
    'TC-004': 'Source hiện tại chuyển URL cũ sang /product/list?categoryId=...; test đối chiếu chuyển hướng và lọc tại URL mới.',
    'TC-007': 'Validation khoảng giá hiện nằm tại /product/list sau khi URL list-by-price chuyển hướng.',
    'TC-063': 'Script xác nhận giá mới lấy từ DB; chưa đánh giá thủ công trải nghiệm người dùng khi giá thay đổi.',
    'TC-065': 'Script gửi lặp tuần tự cùng checkoutKey; chưa stress hai request đồng thời.',
    'TC-122': 'Compile đã được Maven thực thi trước test; contextLoads kiểm tra profile test kết nối H2. Lỗi build imageFile của snapshot cũ không còn là hiện trạng.',
}

md = f'''# KẾT QUẢ TEST CASE Y3 / Y4 - MỘC STORE

Tạo báo cáo: {timestamp}. Nguồn: XML JUnit do Maven Surefire tạo, không điền Pass thủ công.

**{len(results)} lượt test tự động: {counts['Pass']} Pass, {counts['Fail']} Fail, {counts['Skipped']} Skipped.**
Đối chiếu với 122 Test Case Y2: {len(by_case)} case có script và có kết quả; {122-len(by_case)} case chưa chạy tự động.

## Cách đọc và giới hạn

- Pass trong tài liệu này là **Pass của phạm vi assertions trong script**, không xác nhận mọi bước/biến thể thủ công của Test Case Y2 đã được thực hiện. Tên phương thức và XML cho biết chính xác phần đã chạy.
- Một Test Case Y2 có thể được nhiều script kiểm tra; test tham số có thể chạy nhiều lượt. Số lượt JUnit không bằng số Test Case Y2.
- DB test là H2 in-memory, MODE=MSSQLServer; MailService là mock. Không gửi email thật, không xác nhận toàn bộ hành vi SQL Server/schema/collation/gửi SMTP thật.
- Không đánh dấu 3 case cài mới/dữ liệu mẫu/đánh giá giao diện là Pass khi chưa thực hiện. Case có kết quả Fail được giữ nguyên để phản ánh lỗi thật, không đổi expectation chỉ để xanh.
- Đề gốc: Assignment (1).pdf Y3/Y4; business rule: Assignment Lập trình Java 5 và source hiện tại. Mã TC giữ theo catalogue Y2 đã lưu trong Git. Catalogue cũ là bản tham chiếu; ghi chú cập nhật bên dưới thay thế nhận định hiện trạng đã lỗi thời.

## Tổng hợp theo Test Case Y2

| ID | Nội dung | Kết quả phạm vi tự động | Lượt script |
|---|---|---|---:|
'''
status_by_case = {}
for case in catalogue:
    tests = by_case.get(case['id'], [])
    status = ('Fail' if any(t['status'] == 'Fail' for t in tests) else
              'Skipped' if tests and all(t['status'] == 'Skipped' for t in tests) else
              'Pass' if tests and all(t['status'] == 'Pass' for t in tests) else 'Partial' if tests else 'Not Run')
    status_by_case[case['id']] = status
    md += f"| {case['id']} | {case['title']} | {status} | {len(tests)} |\n"
md += '\n## Chi tiết đối chiếu và kết quả thực tế\n'
for case in catalogue:
    caseid = case['id']
    tests = by_case.get(caseid, [])
    md += f"\n### {caseid} - {case['title']}\n\n"
    for label, key in [('Yêu cầu','req'),('Tiền điều kiện Y2','pre'),('Dữ liệu Y2','data'),('Bước thực hiện Y2','steps'),('Kỳ vọng Y2','expected')]:
        value = case[key]
        if key == 'expected' and caseid == 'TC-085':
            value = 'Ảnh hợp lệ được xử lý và tên file được lưu cùng sản phẩm; không ghi đè file ngoài vùng lưu trữ.'
        if key == 'expected' and caseid == 'TC-122':
            value = 'Source compile thành công; Spring test context chạy với DB kiểm thử, không dùng SQL Server thật.'
        md += f'- **{label}:** {value}\n'
    md += f"- **Kết quả phạm vi tự động:** {status_by_case[caseid]}\n"
    if caseid in notes:
        md += f'- **Phạm vi/ghi chú:** {notes[caseid]}\n'
    if not tests:
        md += f"- **Thực tế:** {manual.get(caseid, 'Chưa có execution XML cho case này; không suy ra kết quả.')}\n"
    for test in tests:
        methodref = test['classname'] + '#' + test['name']
        md += f"- **Script:** `{methodref}` → **{test['status']}** ({test['seconds']} giây). [XML]({test['evidence']}).\n"
        if test['error']:
            md += '- **Thực tế lỗi:** ' + test['error'].replace('\n',' ')[:1200] + '\n'
    md += '- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.\n'

(OUT / 'Test_Cases_Y3_Y4.md').write_text(md, encoding='utf-8')
payload = dict(generated_at=timestamp, counts=dict(counts), total=len(results), mapped_cases=len(by_case),
               case_status=status_by_case, tests=results,
               source_sha256={str(p.relative_to(ROOT)).replace('\\','/'):hashlib.sha256(p.read_bytes()).hexdigest() for p in sources.values()})
(OUT / 'execution-results.json').write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding='utf-8')
rows = ''
for t in results:
    rows += '<tr><td>'+html.escape(', '.join(t['ids']))+'</td><td>'+html.escape(t['classname'].split('.')[-1]+'#'+t['name'])+'</td><td class="'+t['status']+'">'+t['status']+'</td><td>'+str(t['seconds'])+'</td><td><a href="'+t['evidence']+'">XML</a>'+html.escape(t['error'][:700])+'</td></tr>'
page = f'''<!doctype html><html lang="vi"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Kết quả Y3 Y4 - Mộc Store</title>
<style>body{{font:15px/1.6 'Segoe UI',sans-serif;margin:32px;background:#f8faf7;color:#20332b}}h1{{color:#385a42}}table{{border-collapse:collapse;width:100%;background:white}}td,th{{border:1px solid #d5dfd5;padding:10px;text-align:left;overflow-wrap:anywhere}}th{{background:#e7eee4}}.Pass{{color:#166534;font-weight:bold}}.Fail{{color:#b91c1c;font-weight:bold}}input{{padding:10px;width:min(600px,90%);margin:14px 0}}.summary{{padding:18px;background:#e7eee4;border-radius:8px}}@media print{{input{{display:none}}body{{margin:0;font-size:10pt}}}}</style>
<h1>Kết quả JUnit & Selenium · Mộc Store</h1><p>{html.escape(timestamp)}</p>
<div class="summary"><b>{len(results)} lượt test: {counts['Pass']} Pass · {counts['Fail']} Fail · {counts['Skipped']} Skipped</b><br>{len(by_case)}/122 case Y2 có script đã chạy. Phạm vi: Mockito + H2 trong bộ nhớ + Chrome Selenium; SMTP giả lập.</div>
<p>Pass chỉ áp dụng assertions trong script. Không xác nhận SQL Server/SMTP thật hoặc mọi biến thể thủ công. <a href="Test_Cases_Y3_Y4.md">Xem Test Case và phạm vi chi tiết</a> · <a href="HUONG_DAN_Y3_Y4.md">Hướng dẫn chạy</a></p>
<label for="filter">Lọc theo ID, tên test hoặc kết quả:</label><br><input id="filter" placeholder="Ví dụ: TC-074, Selenium, Fail"><table><thead><tr><th>Case Y2</th><th>Test script</th><th>Kết quả</th><th>Giây</th><th>Bằng chứng/lỗi</th></tr></thead><tbody>{rows}</tbody></table>
<script>document.getElementById('filter').addEventListener('input',function(){{const q=this.value.toLowerCase();document.querySelectorAll('tbody tr').forEach(r=>r.hidden=!r.textContent.toLowerCase().includes(q));}});</script></html>'''
(OUT / 'Execution_Report.html').write_text(page, encoding='utf-8')
print(json.dumps(dict(tests=len(results),counts=dict(counts),mapped_cases=len(by_case),not_run=[k for k,v in status_by_case.items() if v=='Not Run']),ensure_ascii=False))
