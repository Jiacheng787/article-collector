import axios from 'axios';

const service = axios.create({
    baseURL: '/',
    timeout: 1000000, // 请求超时时间
})

service.interceptors.request.use(
    (config) => {
        // 后期做登录功能，向请求头中添加 token
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
)

service.interceptors.response.use(
    (response) => {
        const { headers, data, status } = response;
        if (status == '200' || data.success) {
            return data.data;
        } else {
            return Promise.reject();
        }
    },
    (error) => {
        if (error && error.response) {
          // 使用后端回传的错误信息
          const { message } = error.response.data;
          switch (error.response.status) {
              case 400:
                error.message = message || '请求错误';
                break;
              case 401:
                error.message = message || '未授权，请登录';
              //   if (!window.location.href.includes('login')) {
              //     router.push('/login');
              //   }
                break;
              case 403:
                error.message = message || '拒绝访问';
                break;
              case 404:
                error.message = message || `请求地址出错: ${error.response.config.url}`;
                break;
              case 500:
                error.message = message || '服务器内部错误';
                break;
              case 503:
                error.message = message || '网络异常，请检查后端服务状态';
                break;
              default:
                break;
          }
        }
        return Promise.reject(error);
    }
)

export default service;