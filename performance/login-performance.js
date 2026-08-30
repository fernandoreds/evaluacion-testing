import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 5,
  duration: '10s',

  thresholds: {
    http_req_duration: ['p(95)<1'],
    http_req_failed: ['rate<0.05'],
  },
};

export default function () {
  const payload = JSON.stringify({
    usuario: 'fernando',
    password: '1234',
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const response = http.post(
    'https://httpbin.org/post',
    payload,
    params
  );

  check(response, {
    'respuesta HTTP 200': (r) => r.status === 200,
    'respuesta menor a 1 segundo': (r) => r.timings.duration < 1000,
  });

  sleep(1);
}