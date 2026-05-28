# 单 Agent ADD 3.0 作业项目

本项目实现了作业要求中的 **Option 2: Single-agent（顺序推理 + 自我反思）**。程序使用 ADD 3.0 方法，对 Hotel Pricing System 案例进行软件体系结构设计。

## 提交材料位置

根据作业要求，关键单 Agent 交付物位置如下：

- 源代码：当前 GitHub 仓库。
- 带时间戳的完整 LLM 对话日志：`outputs/origin(English)/conversation-log.jsonl`。
- 可读 Markdown 版本对话日志：`outputs/origin(English)/conversation-log.md`。
- 英文报告源文件：`outputs/origin(English)/report-draft.md`。
- 最终 PDF 报告：`report.pdf`。
- 中文参考版本：`outputs/chinese/`。

## 运行环境

- Java 17+
- Maven 3.9+
- Spring Boot
- Spring AI Alibaba DashScope starter
- 模型：`qwen3-32b`

## 配置方式

运行前需要设置 DashScope API Key：

```bash
export AI_DASHSCOPE_API_KEY=your_api_key
```

程序也支持较短的环境变量名：

```bash
export DASHSCOPE_API_KEY=your_api_key
```

请确保环境变量与 Maven 命令在同一个终端会话中执行：

```bash
echo $AI_DASHSCOPE_API_KEY
mvn spring-boot:run
```

模型配置位于 `src/main/resources/application.yml`：

```yaml
spring:
  ai:
    dashscope:
      read-timeout: 180000
      chat:
        options:
          model: qwen3-32b
          enable-thinking: false
```

作业要求显式体现 self-reflection，因此程序通过独立的 self-reflection 调用完成自我反思；这里关闭 `enable-thinking` 是为了降低请求延迟，不影响单 Agent 自我反思流程。

## 运行方式

```bash
mvn spring-boot:run
```

可选：指定输出目录：

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--assignment.output-dir=outputs"
```

## 输出文件

程序成功运行后会生成：

- `outputs/conversation-log.jsonl`
- `outputs/add-results.md`
- `outputs/report-draft.md`

在当前提交版本中，英文原始输出已归档到 `outputs/origin(English)/`，中文可读参考文件已放到 `outputs/chinese/`。

## 测试

```bash
mvn test
```
