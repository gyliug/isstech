-- 菜单 SQL
insert into sys_menu (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息推送', 152, 1, 'infoPush', 'msg/infoPush/index', 'C', '0', '', 'star', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '消息推送菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息推送新增', @parentId, 1, '',  '#', 'F', '0', 'infoPush_add',          '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息推送修改', @parentId, 2, '',  '#', 'F', '0', 'infoPush_edit',         '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息推送删除', @parentId, 3, '',  '#', 'F', '0', 'infoPush_del',       '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息推送查询', @parentId, 4, '',  '#', 'F', '0', 'infoPush_view',         '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');

insert into sys_menu  (name, parent_id, sort, path, component, type, status, perms, icon, create_by, create_time, update_by, update_time, remarks)
values('消息推送导出', @parentId, 5, '',  '#', 'F', '0', 'infoPush_export',       '#', 'entfrm', '2020-02-02', 'entfrm', '2020-02-02', '');
