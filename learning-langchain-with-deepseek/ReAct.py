# 导入环境变量
from dotenv import load_dotenv
load_dotenv()

# 初始化大模型
# 初始化 DeepSeek 模型
from langchain_openai import ChatOpenAI
# llm = ChatDeepSeek(
#     model="deepseek-r1",
#     temperature=0.5,
#     api_key="sk-5425b506eeea42b0b02e1a17e4300e36",
#     base_url="https://api.deepseek.com"
# )
llm = ChatOpenAI(
    model="deepseek-r1", 
    openai_api_key="sk-40e23384ecbb4bdf9ff465d006b5dd38", 
    openai_api_base="https://dashscope.aliyuncs.com/compatible-mode/v1", 
    max_tokens=1024
)

# 定义Bocha Web Search工具
from langchain.agents import initialize_agent, Tool, AgentType
from langchain.tools import tool
import requests

BOCHA_API_KEY="sk-14f7f4d0ac764039a99afa5173d126dd"

@tool
def bocha_websearch_tool(query: str, count: int = 10) -> str:
    """
    使用Bocha Web Search API 进行网页搜索。

    参数:
    - query: 搜索关键词
    - freshness: 搜索的时间范围
    - summary: 是否显示文本摘要
    - count: 返回的搜索结果数量

    返回:
    - 搜索结果的详细信息，包括网页标题、网页URL、网页摘要、网站名称、网站Icon、网页发布时间等。
    """
    
    url = 'https://api.bochaai.com/v1/web-search'
    headers = {
        'Authorization': f'Bearer {BOCHA_API_KEY}',  # 请替换为你的API密钥
        'Content-Type': 'application/json'
    }
    data = {
        "query": query,
        "freshness": "noLimit", # 搜索的时间范围，例如 "oneDay", "oneWeek", "oneMonth", "oneYear", "noLimit"
        "summary": True, # 是否返回长文本摘要
        "count": count
    }

    response = requests.post(url, headers=headers, json=data)

    if response.status_code == 200:
        json_response = response.json()
        try:
            if json_response["code"] != 200 or not json_response["data"]:
                return f"搜索API请求失败，原因是: {response.msg or '未知错误'}"
            
            webpages = json_response["data"]["webPages"]["value"]
            if not webpages:
                return "未找到相关结果。"
            formatted_results = ""
            for idx, page in enumerate(webpages, start=1):
                formatted_results += (
                    f"引用: {idx}\n"
                    f"标题: {page['name']}\n"
                    f"URL: {page['url']}\n"
                    f"摘要: {page['summary']}\n"
                    f"网站名称: {page['siteName']}\n"
                    f"网站图标: {page['siteIcon']}\n"
                    f"发布时间: {page['dateLastCrawled']}\n\n"
                )
            return formatted_results.strip()
        except Exception as e:
            return f"搜索API请求失败，原因是：搜索结果解析失败 {str(e)}"
    else:
        return f"搜索API请求失败，状态码: {response.status_code}, 错误信息: {response.text}"

# 创建LangChain工具
bocha_tool = Tool(
    name="BochaWebSearch",
    func=bocha_websearch_tool,
    description="使用Bocha Web Search API 进行搜索互联网网页，输入应为搜索查询字符串，输出将返回搜索结果的详细信息，包括网页标题、网页URL、网页摘要、网站名称、网站Icon、网页发布时间等。"
)

# 设置工具
from langchain.agents import load_tools
tools = [bocha_tool] + load_tools(["llm-math"], llm=llm)

# 设置提示模板
from langchain.prompts import PromptTemplate
template = ('''
    '尽你所能用中文回答以下问题。如果能力不够你可以使用以下工具:\n\n'
    '{tools}\n\n
    Use the following format:\n\n'
    'Question: the input question you must answer\n'
    'Thought: you should always think about what to do\n'
    'Action: the action to take, should be one of [{tool_names}]\n'
    'Action Input: the input to the action\n'
    'Observation: the result of the action\n'
    '... (this Thought/Action/Action Input/Observation can repeat N times)\n'
    'Thought: I now know the final answer\n'
    'Final Answer: the final answer to the original input question\n\n'
    'Begin!\n\n'
    'Question: {input}\n'
    'Thought:{agent_scratchpad}' 
    '''
)
prompt = PromptTemplate.from_template(template)

# 初始化Agent
from langchain.agents import create_react_agent
agent = create_react_agent(llm, tools, prompt)

# 构建AgentExecutor
from langchain.agents import AgentExecutor
agent_executor = AgentExecutor(agent=agent, 
                               tools=tools, 
                               handle_parsing_errors=True,
                               verbose=True)

# 执行AgentExecutor
agent_executor.invoke({"input": 
                       """目前市场上玫瑰花的一般进货价格是多少？\n
                       如果我在此基础上加价5%，应该如何定价？"""})
