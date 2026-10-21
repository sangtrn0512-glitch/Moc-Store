package org.example.asm_java5.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import org.example.asm_java5.entity.Account;
import org.example.asm_java5.service.FileService;
import org.example.asm_java5.support.*;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class UploadIntegrationTests extends DatabaseTestSupport {
  @MockitoBean FileService files;
  MockMvc mvc;
  @BeforeEach void mvc() {mvc=MockMvcBuilders.webAppContextSetup(context).build();}
  MockHttpSession session(Account a) {
    MockHttpSession s=new MockHttpSession(); s.setAttribute("csrf","token");
    s.setAttribute("username",a.getUsername()); s.setAttribute("authStamp",a.getPassword()); return s;
  }
  MockMultipartFile image(String name) throws Exception {
    ByteArrayOutputStream bytes=new ByteArrayOutputStream(); ImageIO.write(new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB),"png",bytes);
    return new MockMultipartFile(name,"qa.png","image/png",bytes.toByteArray());
  }
  @Test @CaseIds("TC-044") void profileUploadSavesReturnedFilename() throws Exception {
    when(files.save(any())).thenReturn("qa-profile.png");
    mvc.perform(multipart("/account/photo").file(image("photo")).session(session(user)).param("csrf","token"))
        .andExpect(flash().attributeExists("success"));
    assertEquals("qa-profile.png",accounts.findById(user.getUsername()).orElseThrow().getPhoto()); verify(files).save(any());
  }
  @Test @CaseIds("TC-085") void productUploadSavesReturnedFilename() throws Exception {
    when(files.save(any())).thenReturn("qa-product.png");
    mvc.perform(multipart("/admin/product/create").file(image("imageFile")).session(session(admin)).param("csrf","token")
        .param("name","Upload QA").param("price","100000").param("discount","0").param("categoryId",c1.getId().toString()))
        .andExpect(flash().attributeExists("success"));
    assertEquals("qa-product.png",products.findAll().stream().filter(p->p.getName().equals("Upload QA")).findFirst().orElseThrow().getImage());
    verify(files).save(any());
  }
}
