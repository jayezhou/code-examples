import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const LIST: AppRouteRecordRaw = {
  path: '/list',
  name: 'list',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '列表页',
    requiresAuth: true,
    icon: 'icon-list',
    order: 2,
  },
  children: [
    {
      path: 'search-table', // The midline path complies with SEO specifications
      name: 'SearchTable',
      component: () => import('@/views/list/search-table/index.vue'),
      meta: {
        // hideInMenu: true,
        locale: '查询表格',
        requiresAuth: true,
        roles: ['*'],
      },
    },
    {
      path: 'detail', // The midline path complies with SEO specifications
      name: 'Detail',
      // props: {default: true},
      component: () => import('@/views/list/detail/index.vue'),
      meta: {
        hideInMenu: true,
        locale: '详情',
        requiresAuth: true,
        roles: ['*'],
      },
    },
  ],
};

export default LIST;
