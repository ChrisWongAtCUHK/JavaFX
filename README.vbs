Set WshShell = WScript.CreateObject("WScript.Shell")
' Add the List and Table Views
Return = WshShell.Run("chrome.exe http://docs.oracle.com/javafx/scenebuilder/1/get_started/add-list-table-views.htm", 1)
' The JavaFX Advantage for Swing Developlers
Return = WshShell.Run("chrome.exe http://docs.oracle.com/javafx/2/swing/overview.htm", 1)
' Drag-and-Drop Feature in JavaFX Applications
Return = WshShell.Run("chrome.exe http://docs.oracle.com/javafx/2/drag_drop/jfxpub-drag_drop.htm", 1)
' Getting Started with JavaFX Scene Builder 1.0 
Return = WshShell.Run("chrome.exe http://docs.oracle.com/javafx/scenebuilder/1/get_started/compile-run.htm", 1)
' Handling JavaFX Events 
'	3 Working with Event Filters
Return = WshShell.Run("chrome.exe http://docs.oracle.com/javafx/2/events/filters.htm", 1)
