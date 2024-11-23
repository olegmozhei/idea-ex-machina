package org.oleg.iem

import com.intellij.openapi.components.service
import com.intellij.openapi.components.services
import javax.swing.event.DocumentListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBPopupMenu
import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import org.oleg.iem.listeners.LlmRequestProcessedListener
import org.oleg.iem.listeners.LlmRequestReceivedListener
import org.oleg.iem.listeners.LlmResponseReadyListener
import org.oleg.iem.services.lmm.AskLLMRequest
import org.oleg.iem.utils.AskLLMResponse
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Font
import java.nio.file.Paths
import javax.swing.*
import javax.swing.event.DocumentEvent

object MyToolWindow : LlmResponseReadyListener, LlmRequestProcessedListener {
    private val myPanel = JPanel(BorderLayout())  // main container that can hold other UI components
    private val chatArea = JTextArea(20, 50)  // displays chat messages
    var project: Project? = null

    init {
        // Custom dropdown menu simulation using a button and popup menu
        val settingsButton = JButton("Settings")

        // Popup menu with custom background items
        val popupMenu = JBPopupMenu()

        println("Creating tool window content...")
        myPanel.layout = BoxLayout(myPanel, BoxLayout.Y_AXIS)

        val apiEndpointItem = JMenuItem("API endpoint")
        val contextPathItem = JMenuItem("Context path")
        val minScoreItem = JMenuItem("Min Score")
        val maxResults = JMenuItem("Max Results")

        popupMenu.add(apiEndpointItem)
        popupMenu.add(contextPathItem)
        popupMenu.add(minScoreItem)
        popupMenu.add(maxResults)

        // Display popup when button is clicked
        settingsButton.addActionListener {
            popupMenu.show(settingsButton, 0, settingsButton.height)
        }

        minScoreItem.addActionListener {
            val input = JOptionPane.showInputDialog(
                myPanel,
                "Enter Min Score for context",
                "Min Score Configuration",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                EMBEDDING_MIN_SCORE
            ) as String?
            if (!input.isNullOrBlank()){
                EMBEDDING_MIN_SCORE = input
            }
        }

        maxResults.addActionListener {
            val input = JOptionPane.showInputDialog(
                myPanel,
                "Enter Max Context Results",
                "Max Results Configuration",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                EMBEDDING_MAX_RESULTS
            ) as String?
            if (!input.isNullOrBlank()){
                EMBEDDING_MAX_RESULTS = input
            }
        }

        apiEndpointItem.addActionListener {
            val input = JOptionPane.showInputDialog(
                myPanel,
                "Enter API Endpoint",
                "API Endpoint Configuration",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                API_ENDPOINT
            ) as String?
            if (!input.isNullOrBlank()){
                API_ENDPOINT = input
                println("API endpoint updated to: $API_ENDPOINT")
            }
        }

        // Add action for "Context path" item
        contextPathItem.addActionListener {
            showPathConfigurationDialog()
        }

        // Panel to hold menu bar and align it to the left
        val topPanel = JPanel(BorderLayout())
        topPanel.add(settingsButton, BorderLayout.WEST)  // Align menu to the left
        myPanel.add(topPanel, BorderLayout.NORTH)

        chatArea.isEditable = false  //users can only read from chat area
        val scrollPane = JBScrollPane(chatArea)

        // enable word wrapping:
        chatArea.lineWrap = true
        chatArea.wrapStyleWord = true

        val sendButton = JButton("Send")
        val inputField = createStyledTextArea(sendButton)

        val inputScrollPane = JBScrollPane(inputField)
        inputScrollPane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED

        val sendWithoutContextButton = JButton("Send Without RAG")


        myPanel.add(scrollPane)
        myPanel.add(inputScrollPane)

        // Panel to hold buttons and align them to the left
        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.X_AXIS)
        buttonPanel.add(sendButton)
        buttonPanel.add(sendWithoutContextButton)

        // Align button panel to the left in the main panel
        val bottomPanel = JPanel(BorderLayout())
        bottomPanel.add(buttonPanel, BorderLayout.WEST)

        myPanel.add(bottomPanel, BorderLayout.SOUTH)

        sendButton.addActionListener {
            val message = inputField.text
            if (message.isEmpty()) return@addActionListener

            val request = AskLLMRequest.newBuilder()
                .query(message)
                .useRAG(true)
                .build()

            val requestPublisher: LlmRequestReceivedListener = project!!.messageBus
                .syncPublisher(LlmRequestReceivedListener.LLM_REQUEST_RECEIVED_TOPIC)
            requestPublisher.requestReceived(request)

            inputField.text = ""
        }

        sendWithoutContextButton.addActionListener {
            val message = inputField.text
            if (message.isEmpty()) return@addActionListener

            val request = AskLLMRequest.newBuilder()
                .query(message)
                .useRAG(false)
                .build()

            val requestPublisher: LlmRequestReceivedListener = project!!.messageBus
                .syncPublisher(LlmRequestReceivedListener.LLM_REQUEST_RECEIVED_TOPIC)
            requestPublisher.requestReceived(request)

            inputField.text = ""
        }

        addMessageToChat("System: Welcome to the chat!")
    }

    fun askLlmAndAddMessageToToChat(query: String,
                                    promptTemplate: String,
                                    contextData: HashMap<String, String>) {
        contextData["query"] = query

        val request = AskLLMRequest.newBuilder()
            .query(query)
            .useRAG(true)
            .promptTemplate(promptTemplate)
            .contextData(contextData)
            .build()

        val requestPublisher: LlmRequestReceivedListener = project!!.messageBus
            .syncPublisher(LlmRequestReceivedListener.LLM_REQUEST_RECEIVED_TOPIC)
        requestPublisher.requestReceived(request)
    }

    fun addMessageToChat(message: String) {
        chatArea.append("$message\n")
    }

    fun getContent(): JComponent {
        return myPanel
    }

    private fun createStyledTextArea(button: JButton): JTextArea {
        val inputField = JTextArea(3, 40)
        inputField.lineWrap = true
        inputField.wrapStyleWord = true
        inputField.background = JBColor.DARK_GRAY
        inputField.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Gray._200, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        )
        inputField.font = Font("SansSerif", Font.PLAIN, 14)
        inputField.foreground = JBColor.WHITE
        inputField.caretColor = JBColor.WHITE
        inputField.document.addDocumentListener(object: DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) {
                checkTextLength()
            }

            override fun removeUpdate(e: DocumentEvent?) {
                checkTextLength()
            }

            override fun changedUpdate(e: DocumentEvent?) {
                checkTextLength()
            }

            private fun checkTextLength() {
                // TODO: Think about query restrictions/transformations
                // for searching through RAG database efficiently
                // button.isEnabled = inputField.text.length <= 1000
            }
        })
        return inputField
    }

    override fun responseReceived(response: AskLLMResponse) {
        println("Got message from messaging infrastructure")
        addMessageToChat("LLM: ${response.llmResponse}")
    }

    override fun requestProcessed(request: AskLLMRequest) {
        addMessageToChat("You: ${request.prompt}")
    }

    fun showPathConfigurationDialog(){
        // Create dialog
        val dialog = JDialog(null as JFrame?, "Context Path Configuration", true)
        dialog.layout = BorderLayout()
        dialog.setSize(400, 150)
        dialog.setLocationRelativeTo(null)

        val inputPanel = JPanel(FlowLayout())
        inputPanel.add(JLabel("Enter Context Path"))
        val projectService = project!!.service<MySettings>()

        val pathToShow = if (projectService.state.PATH_TO_PROJECT_CONTEXT == "") Paths.get("")
            .toAbsolutePath()
            .toString() else projectService.state.PATH_TO_PROJECT_CONTEXT
        val pathField = JTextField(pathToShow, 20)
        inputPanel.add(pathField)

        // Error label
        val errorLabel = JLabel("")
        errorLabel.foreground = JBColor.RED

        // Button panel
        val buttonPanel = JPanel(FlowLayout())
        val okButton = JButton("OK")
        val cancelButton = JButton("Cancel")

        dialog.isVisible = true
    }
}