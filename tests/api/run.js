import axios from 'axios';

const BASE_URL = process.env.API_BASE_URL || 'http://localhost:8080/api/v1';

function assert(cond, msg) {
  if (!cond) throw new Error(msg);
}

async function login(username, password) {
  const { data } = await axios.post(`${BASE_URL}/auth/login`, { username, password });
  assert(data && data.token, 'Login no devolvió token');
  assert(data.usuario, 'Login no devolvió usuario');
  assert(data.rol, 'Login no devolvió rol');
  return data.token;
}

async function authClient(token) {
  const client = axios.create({ baseURL: BASE_URL, timeout: 10000 });
  client.interceptors.request.use(cfg => {
    cfg.headers.Authorization = `Bearer ${token}`;
    return cfg;
  });
  return client;
}

async function testProducts(api) {
  const list = await api.get('/products');
  assert(Array.isArray(list.data), 'Products no devolvió lista');
  if (list.data.length) {
    const id = list.data[0].id;
    const one = await api.get(`/products/${id}`);
    assert(one.data && one.data.id === id, 'Product detalle no coincide');
  }
  console.log('✔ Products OK');
}

async function testOrders(api) {
  const list = await api.get('/orders');
  assert(Array.isArray(list.data), 'Orders no devolvió lista');
  console.log('✔ Orders OK');
}

async function testUsers(api) {
  const list = await api.get('/users');
  assert(Array.isArray(list.data), 'Users no devolvió lista');
  console.log('✔ Users OK');
}

async function main() {
  try {
    const adminUser = process.env.API_USER || 'admin';
    const adminPass = process.env.API_PASS || 'Admin123!';
    const token = await login(adminUser, adminPass);
    const api = await authClient(token);

    await testProducts(api);
    await testOrders(api);
    await testUsers(api);

    console.log('\nTodas las pruebas pasaron.');
    process.exit(0);
  } catch (err) {
    console.error('Prueba falló:', err.message);
    process.exit(1);
  }
}

main();
