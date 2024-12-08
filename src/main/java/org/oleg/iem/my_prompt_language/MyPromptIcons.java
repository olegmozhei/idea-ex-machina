package org.oleg.iem.my_prompt_language;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/*
Sizes for other icon sizes can be found in the table below:

Icon Type                                    |   Icons Size    |   Transparent Border
Gutter, status bar                           |   12px x 12px   |          —
Tool window                                  |   13px x 13px   |          —
Default: toolbar icons, project tree, etc.   |   16px x 16px   | 1px, except for modifier
Dialogs                                      |   32px x 32px   |          2px
Logo app icon                                |   32px x 32px
                                                 64px x 64px
                                                128px x 128px
                                                256px x 256px
                                                512px x 512px
 */

public class MyPromptIcons {
    public static final Icon FILE = IconLoader.getIcon("/icons/file_icon_16px.png", MyPromptIcons.class);
    public static final Icon RUN_REQUEST = IconLoader.getIcon("/icons/run_request_16px.png", MyPromptIcons.class);

}
