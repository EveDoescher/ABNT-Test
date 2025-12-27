package com.doescher.ABNT.Formatters;

import com.doescher.ABNT.Domain.Models.Document;
import com.doescher.ABNT.Engine.WordEngine;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public interface ComponentFormatter {
    void format(XWPFDocument doc, Document data, WordEngine engine);

    boolean shouldRender(Document data);
}
