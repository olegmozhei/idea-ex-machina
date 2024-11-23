package org.oleg.iem

import com.intellij.openapi.components.*


@Service(Service.Level.PROJECT)
@State(name = "MyPluginSettings", storages = [Storage("MyPluginSettings.xml")])
class MySettings : SimplePersistentStateComponent<MyState>(MyState())

class MyState : BaseState() {
    // Vector Database:
    var QDRANT_HOST by string("localhost")
    var QDRANT_PORT by property(6334)

    var PATH_TO_PROJECT_CONTEXT by string("")
}