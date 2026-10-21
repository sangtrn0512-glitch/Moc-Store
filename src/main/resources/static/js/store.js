document.documentElement.classList.add('js');
const toggle = document.querySelector('.menu-toggle');
const navigation = document.querySelector('#site-navigation');
if (toggle && navigation) {
  toggle.addEventListener('click', () => {
    const expanded = toggle.getAttribute('aria-expanded') !== 'true';
    toggle.setAttribute('aria-expanded', String(expanded));
    navigation.classList.toggle('is-open', expanded);
  });
  document.addEventListener('keydown', event => {
    if (event.key === 'Escape' && toggle.getAttribute('aria-expanded') === 'true') {
      navigation.classList.remove('is-open');
      toggle.setAttribute('aria-expanded', 'false');
      toggle.focus();
    }
  });
}
document.querySelectorAll('.main-nav a, .admin-nav a').forEach(link => {
  const path = window.location.pathname;
  if (link.pathname === path || (link.pathname === '/product/list' && path.startsWith('/product/')) || (link.pathname === '/order/list' && path.startsWith('/order/'))) {
    link.setAttribute('aria-current', 'page');
  }
});
const filterButtons = document.querySelectorAll('[data-filter]');
filterButtons.forEach(button => button.addEventListener('click', () => {
  filterButtons.forEach(item => item.setAttribute('aria-pressed', String(item === button)));
  let count = 0;
  document.querySelectorAll('[data-category]').forEach(card => {
    card.hidden = button.dataset.filter !== 'all' && card.dataset.category !== button.dataset.filter;
    if (!card.hidden) count++;
  });
  document.querySelector('.catalog-count').textContent = `Hiển thị ${count} sản phẩm`;
}));

const initialCategory = new URLSearchParams(window.location.search).get('category');
if (initialCategory) {
  const initialFilter = [...filterButtons].find(button => button.dataset.filter === initialCategory);
  if (initialFilter) initialFilter.click();
}
