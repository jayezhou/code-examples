export interface RoleCountModel {
  id: number;
  name: string;
  count: number;
}

export interface RoleCountModelArray {
  [index: number]: RoleCountModel
}

export interface RoleCountModelResult {
  roleCounts: Array<RoleCountModel>
}
