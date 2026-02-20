export function springPageParams(pageIndex: number, pageSize: number, sort?: string) {
  const params: any = { page: pageIndex, size: pageSize };
  if (sort) params.sort = sort;
  return params;
}
