package com.doescher.ABNT.formatters;

import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.helpers.WordHelper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public interface ComponentFormatter {
    void format(XWPFDocument doc, Document data, WordHelper engine);

    boolean shouldRender(Document data);
}
