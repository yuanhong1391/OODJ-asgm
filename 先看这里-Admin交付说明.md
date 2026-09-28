# Admin 接入版本

基于小组 `manager-refactor` 的 `1507346` 提交制作。只新增 Admin 文件，组员原有代码、表单、数据和项目配置均未修改。

## 你现在怎么打开

1. NetBeans → File → Open Project，选择 `D:\Downloads\OODJ Assignment\admin-compatible`。
2. 确认使用 JDK 26，然后右键项目 → Clean and Build。
3. Source Packages → default package → 右键 `AdminLauncher.java` → Run File（Shift+F6）。不要从旧 HMSAdmin 项目运行。
4. 首次启动创建管理员。可以用测试姓名 `Demo Admin`、用户名 `admin.demo`，密码由你自己设置，至少 8 位。
5. 提示框会显示管理员 ID，通常是 `ADM001`。登录时输入这个 **ID** 和你刚设置的密码，不是用户名。

## 发给组员什么

把 `Admin-manager-refactor-addon.zip` 发给负责合并的组员，让他把包内文件放入最新 `manager-refactor` 项目根目录。

同时让他阅读 `ADMIN-INTEGRATION.md` 第 2 节：里面有一段放入现有 `Login.java` 的接入代码。你要求不修改组员文件，所以这段仅提供说明，没有替组员执行。加上以后，原 Main、统一 Login 和各角色的 Logout 就能经过同一个登录页处理 Admin。

无需修改 UserService；不要把 Admin 账号写进小组 users.txt，也不要把旧版 hms 项目或旧 users.txt 混进来。

## 最重要的联合测试

1. Admin → End users → Add：创建一个 Doctor、一个 Patient、一个 Manager。输入不重复的 username 和至少 8 位密码，记下表格中的 ID。
2. 运行小组原 Main，用这些 **ID + Password** 分别登录，确认进入对应角色页面。
3. Doctor 登录后提交一个 Blood Test (Full Blood Count) 检查请求。
4. Admin → Hospital assets：添加一个 LAB 设施，状态 AVAILABLE。
5. Admin → Lab / imaging requests → Refresh：应出现刚才的请求。选中后点击 Schedule selected request，选择设施、填写有效起止时间并保存。
6. 重新打开 Doctor 页面，确认请求状态为 Scheduled。
7. 关闭并重启 Admin，确认用户、资产和排期仍存在。

Admin 用户新增、查看、修改、删除，医生分配，资产与分配，检查请求处理，费率和保险管理均保留。用户有历史记录时会阻止删除；不会删除病人历史来强行完成 CRUD。

## 尚未自动接上的内容

- 默认 Main 仍打开组员原 Login；组员加入说明中的 hook 前，请用 AdminLauncher 登录 Admin。
- 医生分配数据还不会自动改变组员 Manager 页面的筛选。
- 费率和保险已保存，但组员现有账单／报告尚未读取这些设置。
- Admin 排期不等同于 Patient 原来的按日期预约；没有擅自改动组员预约逻辑。

这些限制在英文接入说明中也列明，交付时不要描述成已经完成了所有跨模块业务联动。

## 报告和课堂要求

新版使用 `models.AdminStaff extends models.User`；不要继续拿旧 `hms.model.User` 的图描述它。重新运行后再截图。提供了可供 NetBeans Design 打开的 .form 文件，但仍需你在 NetBeans 检查老师要求的操作方式。

本次包括 AI 辅助修改代码和测试，AI prompt log 应如实记录，不能仅写成纠错。请理解代码，并按老师 Yellow/Restricted 规则确认可使用范围。
