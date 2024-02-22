import type { AppRouteModule } from '@/router/types';

import { LAYOUT } from '@/router/constant';

const example: AppRouteModule = {
  path: '/example',
  name: 'Example',
  component: LAYOUT,
  redirect: '/example/roleCount',
  meta: {
    icon: 'simple-icons:aboutdotme',
    title: '样例',
    orderNo: 100001,
  },
  children: [
    {
      path: 'roleCount',
      name: '角色用户数',
      component: () => import('@/views/example/role-count/index.vue'),
      meta: {
        title: '角色用户数',
      },
    },
  ],
};

export default example;
