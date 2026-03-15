Link to GitHub Repo: `https://github.com/chriskedryna/SE333-Assignment5-P2`

## Reflection comparing Manual UI testing to AI-Assisted

- Ease of writing and running tests
  Overall, I'd give a slight edge to AI-assisted. Manual testing involved me having to have 4 different windows open: (1) while I clicked on buttons, entered values, etc., (2) checked the playwright output so I could use it to generate my tests, then (3) actually write the tests in VS Code, and then (4) I needed the assignment page open to know what inputs to do next. At least with the AI-Assisted, I only needed VS code and the assignment sheet just so I could style the input in a natural-sounding manner. The prompt ended up being pretty long though. While the AI took a while to write the tests, it was still quicker than if I had to. Running the tests didn't have much of a difference between traditional and LLM.

- Accuracy and reliability of generated tests
  The nice thing about writing the tests myself was knowing that they would pass on the first go. I could make sure I entered the right assertions because I had the playwright test window open. As a result, my traditional tests passed on the first try, and aren't flaky or anything. They always pass. The LLM-generated ones, on the other hand, don't pass due to timeout errors. This is because the locator test written by the LLM didn't seem to match up with a UI element, so playwright would wait to find that locator and eventually timed out. This was the first try, before I realized it should use MCP tools. After telling it to use the playwright MCP tools, I still ran into issues, just different ones. Using the MCP tools used tokens and ate away at my context window, so I had to compact the conversation just to write tests. Also, the tests didn't even work.

- Maintenance effort
  The LLM requires more maintenance mainly because the tests didn't pass on the first try. I had to check the outputs and diagnose the errors. When playwright MCP tools are installed, maintenanance was still required to compact the conversation. Apart from that, not much maintenance needed apart from saying `Allow all` to activate YOLO mode.

- Limitations or issues
  Even with multiple tries and using MCP tools, the LLM didn't generate working tests. I could be to blame: perhaps I needed to focus on making my prompt smaller or only giving the AI agent access to a select number of tools so it doesn't use any for no reason. Not really sure. In terms of manual, I just found it tedious to write the tests, but I imagine the process gets smoother the more you do it.
