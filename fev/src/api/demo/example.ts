import { defHttp } from '@/utils/http/axios';
// import { RoleCountArray } from './model/exampleModel';
import { RoleCountModelResult } from './model/exampleModel';

enum Api {
  ROLE_COUNT = '/roleCount',
}

export const roleCountApi = () => defHttp.get<RoleCountModelResult>({ url: Api.ROLE_COUNT });
//export const roleCountApi = () => defHttp.get<RoleCountModel>({ url: Api.ROLE_COUNT });
