package org.example.asm_java5.controller;

import static org.example.asm_java5.support.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;
import org.example.asm_java5.config.AuthInterceptor;
import org.example.asm_java5.controller.admin.CategoryAController;
import org.example.asm_java5.service.*;
import org.example.asm_java5.support.CaseIds;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ControllerTests {
  ProductService products;
  CartService cart;
  AccountService accounts;
  AuthService auth;
  CategoryService categories;
  MailService mail;
  MockMvc mvc;

  @BeforeEach
  void setup() {
    products = mock(ProductService.class);
    cart = mock(CartService.class);
    accounts = mock(AccountService.class);
    auth = mock(AuthService.class);
    categories = mock(CategoryService.class);
    mail = mock(MailService.class);
    mvc =
        MockMvcBuilders.standaloneSetup(
                new ProductController(products, categories),
                new CartController(cart),
                new AccountController(accounts, mail, auth, mock(FileService.class)),
                new AuthController(auth),
                new CategoryAController(categories),
                new HomeController(products))
            .setControllerAdvice(new FormErrors())
            .addInterceptors(new AuthInterceptor(auth))
            .setViewResolvers(
                new org.springframework.web.servlet.view.InternalResourceViewResolver(
                    "/test-views/", ".html"))
            .build();
  }

  MockHttpSession csrf() {
    MockHttpSession s = new MockHttpSession();
    s.setAttribute("csrf", "token");
    return s;
  }

  @Test
  @CaseIds("TC-001")
  void homeSuppliesThreeProductSections() throws Exception {
    mvc.perform(get("/home/index"))
        .andExpect(status().isOk())
        .andExpect(view().name("home"))
        .andExpect(
            model().attributeExists("newProducts", "discountProducts", "bestSellingProducts"));
    verify(products).findNewProducts();
    verify(products).findDiscountProducts();
    verify(products).findBestSellingProducts();
  }

  @Test
  @CaseIds("TC-004")
  void categoryFilterUsesRequestedId() throws Exception {
    mvc.perform(get("/product/list-by-category/7"))
        .andExpect(redirectedUrl("/product/list?categoryId=7"));
    when(products.findAll()).thenReturn(List.of(product(7, "100000", "10")));
    mvc.perform(get("/product/list").param("categoryId", "1"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("products", org.hamcrest.Matchers.hasSize(1)));
  }

  @Test
  @CaseIds("TC-007")
  void invalidPriceRangeShowsError() throws Exception {
    mvc.perform(get("/product/list").param("minPrice", "20").param("maxPrice", "10"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("error"));
    verify(products, never()).findByPriceBetween(any(), any());
  }

  @Test
  @CaseIds("TC-009")
  void detailLoadsRelatedProductsWithoutSelf() throws Exception {
    var p = product(7, "100000", "10");
    when(products.findById(7)).thenReturn(Optional.of(p));
    mvc.perform(get("/product/detail/7"))
        .andExpect(view().name("product-detail"))
        .andExpect(model().attribute("product", p));
    verify(products).findRelatedProducts(1, 7);
  }

  @Test
  @CaseIds("TC-010")
  void missingProductRedirects() throws Exception {
    mvc.perform(get("/product/detail/999")).andExpect(redirectedUrl("/product/list"));
  }

  @Test
  @CaseIds("TC-011")
  void malformedPriceIsBadRequest() throws Exception {
    mvc.perform(get("/product/list-by-price").param("minPrice", "abc").param("maxPrice", "10"))
        .andExpect(status().isBadRequest());
    mvc.perform(get("/product/list-by-price").param("minPrice", "10"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @CaseIds("TC-012")
  void anonymousCartPostWithCsrfWorks() throws Exception {
    mvc.perform(post("/cart/add/1").session(csrf()).param("csrf", "token"))
        .andExpect(redirectedUrl("/product/detail/1"));
    verify(cart).add(1);
  }

  @Test
  @CaseIds("TC-052")
  void missingCsrfPreventsMutation() throws Exception {
    mvc.perform(post("/cart/add/1")).andExpect(status().isForbidden());
    verifyNoInteractions(cart);
  }

  @Test
  @CaseIds("TC-048")
  void guestProtectedPageRedirects() throws Exception {
    mvc.perform(get("/account/edit-profile")).andExpect(redirectedUrl("/auth/login"));
  }

  @Test
  @CaseIds("TC-050")
  void guestAdminRedirects() throws Exception {
    mvc.perform(get("/admin/category/index")).andExpect(redirectedUrl("/auth/login"));
    verifyNoInteractions(categories);
  }

  @Test
  @CaseIds("TC-051")
  void userCannotCreateAdminCategory() throws Exception {
    when(auth.current()).thenReturn(account("qa", false));
    mvc.perform(
            post("/admin/category/create")
                .session(csrf())
                .param("csrf", "token")
                .param("name", "QA"))
        .andExpect(status().isForbidden());
    verifyNoInteractions(categories);
  }

  @Test
  @CaseIds("TC-049")
  void loginReturnsToProtectedPage() throws Exception {
    MockHttpSession session = csrf();
    session.setAttribute("returnUrl", "/order/list");
    when(auth.current()).thenReturn(account("qa", false));
    mvc.perform(
            post("/auth/login")
                .session(session)
                .param("csrf", "token")
                .param("username", "qa")
                .param("password", "Test@1234"))
        .andExpect(redirectedUrl("/order/list"));
    assertNull(session.getAttribute("returnUrl"));
  }

  @Test
  @CaseIds("TC-025")
  void blankRegistrationCannotWrite() throws Exception {
    mvc.perform(
            post("/account/sign-up")
                .session(csrf())
                .param("csrf", "token")
                .param("username", " ")
                .param("fullName", "QA")
                .param("email", "qa@example.invalid")
                .param("password", "Test@1234"))
        .andExpect(view().name("sign-up"))
        .andExpect(model().attributeExists("error"));
    verify(accounts, never()).register(any());
  }

  @Test
  @CaseIds("TC-026")
  void duplicateUsernameCannotWrite() throws Exception {
    when(accounts.existsByUsername("qa")).thenReturn(true);
    mvc.perform(
            post("/account/sign-up")
                .session(csrf())
                .param("csrf", "token")
                .param("username", "qa")
                .param("fullName", "QA")
                .param("email", "qa@example.invalid")
                .param("password", "Test@1234"))
        .andExpect(model().attributeExists("error"));
    verify(accounts, never()).register(any());
  }

  @Test
  @CaseIds("TC-071")
  void duplicateCategoryCannotWrite() throws Exception {
    when(auth.current()).thenReturn(account("admin", true));
    when(categories.existsByName("QA")).thenReturn(true);
    mvc.perform(
            post("/admin/category/create")
                .session(csrf())
                .param("csrf", "token")
                .param("name", " QA "))
        .andExpect(model().attributeExists("error"));
    verify(categories, never()).save(any());
  }

  @Test
  @CaseIds("TC-073")
  void categoryUpdateRejectsLongName() throws Exception {
    when(auth.current()).thenReturn(account("admin", true));
    when(categories.findById(1)).thenReturn(Optional.of(category(1, "QA")));
    mvc.perform(
            post("/admin/category/update")
                .session(csrf())
                .param("csrf", "token")
                .param("id", "1")
                .param("name", "a".repeat(101)))
        .andExpect(model().attributeExists("error"));
    verify(categories, never()).save(any());
  }
}
