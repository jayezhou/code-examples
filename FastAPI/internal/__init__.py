# ### internal 包
# `internal`包通常用于放置内部使用的代码，这些代码不应该暴露给外部用户。它们可能包括管理任务、内部工具、管理接口等。

# 例如：
# - **管理接口**：只有管理员或内部用户可以访问的API端点。
# - **内部工具**：用于管理或维护应用程序的脚本或工具。
#
# 示例
# internal 包中，admin.py中的代码定义了一个管理接口：

# from fastapi import APIRouter

# router = APIRouter()


# @router.post("/")
# async def update_admin():
#     return {"message": "Admin getting schwifty"}