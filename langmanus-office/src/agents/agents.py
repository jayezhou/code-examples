from langgraph.prebuilt import create_react_agent
from langchain_mcp_adapters.client import MultiServerMCPClient
import asyncio

from src.prompts import apply_prompt_template
from src.tools import (
    bash_tool,
    browser_tool,
    crawl_tool,
    python_repl_tool,
    tavily_tool,
)

from .llm import get_llm_by_type
from src.config.agents import AGENT_LLM_MAP

# Create agents using configured LLM types
research_agent = create_react_agent(
    get_llm_by_type(AGENT_LLM_MAP["researcher"]),
    tools=[tavily_tool, crawl_tool],
    prompt=lambda state: apply_prompt_template("researcher", state),
)

browser_agent = create_react_agent(
    get_llm_by_type(AGENT_LLM_MAP["browser"]),
    tools=[browser_tool],
    prompt=lambda state: apply_prompt_template("browser", state),
)


async def mcp_call(user_input):
    async with MultiServerMCPClient(
        {
            "excel-mcp-server": {
                "url": "http://localhost:8000/sse",
                "transport": "sse",
            }
        }
    ) as client:
        agent = create_react_agent(get_llm_by_type(AGENT_LLM_MAP["reporter"]), client.get_tools())
        agent_response = await agent.ainvoke(user_input)
        print(f"---- agent_response: {agent_response} ----")
        return agent_response


reporter_agent = None

# async def initialize_agents():
#     global reporter_agent

#     async with MultiServerMCPClient(
#         {# 确保初始化函数被调用
#             "excel-mcp-server": {
#                 "url": "http://localhost:8000/sse",
#                 "transport": "sse",
#             }
#         }
#     ) as client:
#         reporter_agent = create_react_agent(
#             get_llm_by_type(AGENT_LLM_MAP["reporter"]),
#             tools=client.get_tools(),
#             # prompt=lambda state: apply_prompt_template("reporter", state),
#         )

# asyncio.run(initialize_agents())