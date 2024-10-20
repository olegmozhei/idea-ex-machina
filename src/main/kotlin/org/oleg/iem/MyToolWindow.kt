package org.oleg.iem

import javax.swing.event.DocumentListener
import com.intellij.openapi.project.Project
import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import org.oleg.iem.listeners.LlmRequestProcessedListener
import org.oleg.iem.listeners.LlmRequestReceivedListener
import org.oleg.iem.listeners.LlmResponseReadyListener
import org.oleg.iem.services.lmm.AskLLMRequest
import org.oleg.iem.utils.AskLLMResponse
import java.awt.Font
import javax.swing.*
import javax.swing.event.DocumentEvent

object MyToolWindow : LlmResponseReadyListener, LlmRequestProcessedListener {
    private val myPanel = JPanel()  // main container that can hold other UI components
    private val chatArea = JTextArea(20, 50)  // displays chat messages
    var project: Project? = null

    init {
        println("Creating tool window content...")
        myPanel.layout = BoxLayout(myPanel, BoxLayout.Y_AXIS)
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
        myPanel.add(sendButton)
        myPanel.add(sendWithoutContextButton)

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
        TODO("Not yet implemented")
    }

    override fun requestProcessed(request: AskLLMRequest) {
        TODO("Not yet implemented")
    }
}