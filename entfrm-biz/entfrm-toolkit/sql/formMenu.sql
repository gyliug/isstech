-- 菜单 SQL
insert into sys_menu (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('表单管理', 3, 1, 'form', 'toolkit/form/index', 'C', '0', '', 'star', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '表单管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('表单管理新增', @parentId, 1, '',  '#', 'F', '0', 'form_add',          '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('表单管理修改', @parentId, 2, '',  '#', 'F', '0', 'form_edit',         '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('表单管理删除', @parentId, 3, '',  '#', 'F', '0', 'form_del',       '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('表单管理查询', @parentId, 4, '',  '#', 'F', '0', 'form_view',         '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('表单管理导出', @parentId, 5, '',  '#', 'F', '0', 'form_export',       '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');
