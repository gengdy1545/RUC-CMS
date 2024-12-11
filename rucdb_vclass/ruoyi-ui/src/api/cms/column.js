import axios from "axios";
import request from '@/utils/request'
export function fetchColumns() {
  return request({
      url: '/getFrontRouters',
      method: 'get'
  }).then(response => {
      // 对返回数据进行处理，并返回新的结果
      return response.data.map(col => ({
        ...col,
        del: false, // 初始化子菜单状态
      }));
    }).catch(error => {
      console.error('Error fetching columns:', error);
      throw error; // 确保错误能够被调用方捕获
    });
}

