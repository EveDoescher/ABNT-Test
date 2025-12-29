package com.doescher.ABNT.formatters;

import com.doescher.ABNT.models.entities.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public interface ComponentFormatter {
    void format(XWPFDocument doc, Document data);

    boolean shouldRender(Document data);
}
