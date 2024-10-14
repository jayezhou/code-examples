import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const OTHER: AppRouteRecordRaw = {
  path: '/other',
  name: 'other',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '其他',
    requiresAuth: true,
    icon: 'icon-more-vertical',
    order: 2,
  },
  children: [
    {
      path: 'page-header-nav', // The midline path complies with SEO specifications
      name: 'PageHeaderNav',
      component: () => import('@/views/other/page-header-nav/index.vue'),
      meta: {
        locale: '页头导航',
        requiresAuth: true,
        roles: ['*'],
      },
    },
  ],
};

export default OTHER;
