-- 菜单 SQL
insert into sys_menu (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('示例', 109, 1, 'demo', 'cms/demo/index', 'C', '0', '', 'star', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '示例菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('示例新增', @parentId, 1, '',  '#', 'F', '0', 'demo_add',          '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('示例修改', @parentId, 2, '',  '#', 'F', '0', 'demo_edit',         '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('示例删除', @parentId, 3, '',  '#', 'F', '0', 'demo_del',       '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('示例查询', @parentId, 4, '',  '#', 'F', '0', 'demo_view',         '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('示例导出', @parentId, 5, '',  '#', 'F', '0', 'demo_export',       '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');
