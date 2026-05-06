package com.jmod.jrender.client.gui.options.control;

import com.jmod.jrender.client.gui.options.Option;
import com.jmod.jrender.client.util.Dim2i;

public interface Control<T> {
    Option<T> getOption();

    ControlElement<T> createElement(Dim2i dim);

    int getMaxWidth();
}
