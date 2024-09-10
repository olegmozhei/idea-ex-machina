package org.oleg.iem

import javax.swing.*

object MyToolWindow {
    private val myPanel = JPanel()
    private val chatArea = JTextArea(20, 50)

    init {
        println("Creating tool window content...")
        myPanel.layout = BoxLayout(myPanel, BoxLayout.Y_AXIS)
        chatArea.isEditable = false
        val scrollPane = JScrollPane(chatArea)
        val inputField = JTextField(40)
        val sendButton = JButton("Send")

        myPanel.add(scrollPane)
        myPanel.add(inputField)
        myPanel.add(sendButton)

        sendButton.addActionListener {
            val message = inputField.text
            if (message.isNotEmpty()) {
                addMessageToChat("You: $message")
                inputField.text = ""
            }
        }

        addMessageToChat("System: Welcome to the chat!")
    }

    fun addMessageToChat(message: String) {
        chatArea.append("$message\n")
    }

    fun getContent(): JComponent {
        return myPanel
    }
}