package com.br.framework.core.enumerator;

public enum FrameComponent {
    /**
     * Frame (window) configuration.
     * @see FrameConfig
     */
    FRAME_CONFIG,
    /**
     * Swing library JFrame. Created on JFrameFactory.
     * @see JFrameFactory
     */
    SWING_JFRAME,
    /**
     * Swing library JTable. Created on GridFactory/TableModelFactory.
     * @see GridFactory/TableModelFactory
     */
    SWING_JTABLE,    
    SWING_JSCROLL_EDIT,
    SWING_JSCROLL_GRID,
    SWING_EDIT_PANEL,
    SWING_GRID_PANEL,
    EDIT_GRID_LISTENER,
    MAP_COLUMN_POSITION,
    MAP_EDIT_FIELDS,
    BUTTON_GRID_VIEW,
    TABLE_LISTENER,
    MAP_ATRIB_TO_ALIAS,
    FRAME_QUERY
}
