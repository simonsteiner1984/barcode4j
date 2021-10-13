/*
 * Copyright 2012 Jeremias Maerki.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id: QRLogicImpl.java,v 1.3 2012-02-08 12:59:41 jmaerki Exp $ */

package org.krysalis.barcode4j.impl.qr;

import java.awt.Dimension;
import java.util.Hashtable;

import org.krysalis.barcode4j.output.CanvasProvider;
import org.krysalis.barcode4j.TwoDimBarcodeLogicHandler;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

/**
 * Top-level class for the logic part of the DataMatrix implementation.
 *
 * @version $Id: QRLogicImpl.java,v 1.3 2012-02-08 12:59:41 jmaerki Exp $
 */
public class QRLogicImpl implements QRConstants {

    /**
     * Generates the barcode logic.
     * @param logic the logic handler to receive generated events
     * @param msg the message to encode
     * @param errorCorrectionLevel the error correction level (one of L, M, Q, H)
     * @param encoding the message encoding
     * @param minSize the minimum symbol size constraint or null for no constraint
     * @param maxSize the maximum symbol size constraint or null for no constraint
     */
    public void generateBarcodeLogic(TwoDimBarcodeLogicHandler logic, String msg,
            String encoding,
            char errorCorrectionLevel,
            Dimension minSize, Dimension maxSize, CanvasProvider canvas, boolean swissCross) {

        //TODO ZXing doesn't allow to set minSize/maxSize through its API

        ErrorCorrectionLevel zxingErrLevel = getZXingErrorLevel(errorCorrectionLevel);
        Hashtable hints = createHints(encoding);

        QRCode code = new QRCode();
        try {
            Encoder.encode(msg, zxingErrLevel, hints, code);
        } catch (WriterException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        ByteMatrix matrix = code.getMatrix();

        //finally, paint the barcode
        logic.startBarcode(msg, msg);
        encodeLowLevel(logic, matrix, canvas, swissCross);
        logic.endBarcode();
    }

    static Hashtable createHints(String encoding) {
        Hashtable hints = null;
        if (!"ISO-8859-1".equals(encoding)) {
            hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        return hints;
    }

    static ErrorCorrectionLevel getZXingErrorLevel(char errorCorrectionLevel) {
        ErrorCorrectionLevel zxingErrLevel;
        switch (errorCorrectionLevel) {
        case ERROR_CORRECTION_LEVEL_L:
            zxingErrLevel = ErrorCorrectionLevel.L;
            break;
        case ERROR_CORRECTION_LEVEL_M:
            zxingErrLevel = ErrorCorrectionLevel.M;
            break;
        case ERROR_CORRECTION_LEVEL_Q:
            zxingErrLevel = ErrorCorrectionLevel.Q;
            break;
        case ERROR_CORRECTION_LEVEL_H:
            zxingErrLevel = ErrorCorrectionLevel.H;
            break;
        default:
            throw new IllegalArgumentException(
                    "Invalid error correction level: " + errorCorrectionLevel);
        }
        return zxingErrLevel;
    }

    private void encodeLowLevel(TwoDimBarcodeLogicHandler logic, ByteMatrix matrix, CanvasProvider canvas, boolean swissCross) {
        if (swissCross) {
            clearSwissCrossArea(matrix);
        }

        int symbolWidth = matrix.getWidth();
        int symbolHeight = matrix.getHeight();
        for (int y = 0; y < symbolHeight; y++) {
            logic.startRow();
            for (int x = 0; x < symbolWidth; x++) {
                logic.addBar(matrix.get(x, y) == 1, 1);
            }
            logic.endRow();
        }

        if (swissCross) {
            drawSwissCross(canvas, matrix, logic);
        }
    }

    private static void clearSwissCrossArea(ByteMatrix modules) {
        // The Swiss cross area is supposed to be 7 by 7 mm in the center of
        // the QR code, which is 46 by 46 mm.
        // We clear sufficient modules to make room for the cross.
        int size = modules.getHeight();
        int start = (int) Math.floor((size - 6.8) / 2);
        clearRectangle(modules, start, start, size - 2 * start, size - 2 * start);
    }

    private static void clearRectangle(ByteMatrix modules, int x, int y, int width, int height) {
        for (int iy = y; iy < y + height; iy++) {
            for (int ix = x; ix < x + width; ix++) {
                modules.set(ix, iy, false);
            }
        }
    }

    private void drawSwissCross(CanvasProvider graphics, ByteMatrix modules, TwoDimBarcodeLogicHandler logic) {
        double barWidth = logic.getBarWidth();
        double start = logic.getStartX() + (((modules.getHeight() - 6) * barWidth)) / 2;
        graphics.deviceFillRect(start, start, 6 * barWidth, 6 * barWidth);
        double crossBarWidth = (7 / 6.0) * barWidth;
        double barLen = (35 / 9.0) * barWidth;
        start += 3 * barWidth;
        graphics.deviceFillRectWhite(start - crossBarWidth / 2, start - barLen / 2, crossBarWidth, barLen);
        graphics.deviceFillRectWhite(start - barLen / 2, start - crossBarWidth / 2, barLen, crossBarWidth);
    }
}
