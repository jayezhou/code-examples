import { defHttp } from '@/utils/http/axios';
// import { RoleCountArray } from './model/exampleModel';
import { RoleCountModel } from './model/exampleModel';

enum Api {
  ROLE_COUNT = '/roleCount',
}

export const roleCountApi = () => defHttp.get<Array<RoleCountModel>>({ url: Api.ROLE_COUNT });
//export const roleCountApi = () => defHttp.get<RoleCountModel>({ url: Api.ROLE_COUNT });
