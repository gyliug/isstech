-- 菜单 SQL
insert into sys_menu (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息模板', 152, 1, 'infoTemplate', 'msg/infoTemplate/index', 'C', '0', '', 'star', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '消息模板菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息模板新增', @parentId, 1, '',  '#', 'F', '0', 'infoTemplate_add',          '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息模板修改', @parentId, 2, '',  '#', 'F', '0', 'infoTemplate_edit',         '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息模板删除', @parentId, 3, '',  '#', 'F', '0', 'infoTemplate_del',       '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息模板查询', @parentId, 4, '',  '#', 'F', '0', 'infoTemplate_view',         '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息模板导出', @parentId, 5, '',  '#', 'F', '0', 'infoTemplate_export',       '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');
