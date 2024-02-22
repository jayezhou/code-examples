export interface RoleCountModel {
  id: number;
  name: string;
  count: number;
}

export interface RoleCountArray {
  [index: number]: RoleCountModel
}

