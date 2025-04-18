import asyncio
from langchain_mcp_adapters.client import MultiServerMCPClient
from langgraph.prebuilt import create_react_agent
from langchain_deepseek import ChatDeepSeek
from langchain_openai import ChatOpenAI
 
# 初始化 DeepSeek 大模型客户端
# llm = ChatDeepSeek(
#     model="deepseek-chat",  # 指定 DeepSeek 的模型名称
#     api_key="sk-5425b506eeea42b0b02e1a17e4300e36"  # 替换为您自己的 DeepSeek API 密钥
# )
# 初始化 DeepSeek OpenAI兼容 大模型客户端
# llm = ChatOpenAI(
#     api_key="sk-5425b506eeea42b0b02e1a17e4300e36",
#     base_url="https://api.deepseek.com/v1",
#     model="deepseek-chat"
# )
# 初始化 QWEN OpenAI兼容 大模型客户端
llm = ChatOpenAI(
    api_key="sk-40e23384ecbb4bdf9ff465d006b5dd38",
    base_url="https://dashscope.aliyuncs.com/compatible-mode/v1",
    model="qwen-max-latest"
)


# 解析并输出结果
def print_optimized_result(agent_response):
    """
    解析代理响应并输出优化后的结果。
    :param agent_response: 代理返回的完整响应
    """
    messages = agent_response.get("messages", [])
    steps = []  # 用于记录计算步骤
    final_answer = None  # 最终答案
 
    for message in messages:
        if hasattr(message, "additional_kwargs") and "tool_calls" in message.additional_kwargs:
            # 提取工具调用信息
            tool_calls = message.additional_kwargs["tool_calls"]
            for tool_call in tool_calls:
                tool_name = tool_call["function"]["name"]
                tool_args = tool_call["function"]["arguments"]
                steps.append(f"调用工具: {tool_name}({tool_args})")
        elif message.type == "tool":
            # 提取工具执行结果
            tool_name = message.name
            tool_result = message.content
            steps.append(f"{tool_name} 的结果是: {tool_result}")
        elif message.type == "ai":
            # 提取最终答案
            final_answer = message.content
 
    # 打印优化后的结果
    print("\n计算过程:")
    for step in steps:
        print(f"- {step}")
    if final_answer:
        print(f"\n最终答案: {final_answer}")


async def async_main():
    client = MultiServerMCPClient(
        {# 确保初始化函数被调用
            "excel-mcp-server": {
                "url": "http://localhost:8000/sse",
                "transport": "sse",
            }
        }
    )
    reporter_agent = create_react_agent(
        llm,
        tools=client.get_tools(),
        # prompt=lambda state: apply_prompt_template("reporter", state),
    )
    agent_response = await reporter_agent.ainvoke({"messages": "帮我做一个Excel折线图，主题是一季度的销售额，1月份15000元，2月份20000元，3月份18000元。请以Excel文件的形式给我。"})
    print_optimized_result(agent_response)

if __name__ == "__main__":
    asyncio.run(async_main())
