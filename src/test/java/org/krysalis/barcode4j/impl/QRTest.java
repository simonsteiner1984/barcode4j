/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */
package org.krysalis.barcode4j.impl;

import org.junit.Test;
import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.TextAlignment;
import org.krysalis.barcode4j.impl.qr.QRCodeBean;
import org.krysalis.barcode4j.output.CanvasProvider;

import static org.junit.Assert.assertEquals;

public class QRTest {
    @Test
    public void testGenerateBarcode() {
        QRCodeBean bean = new QRCodeBean();
        MockCanvasProvider canvasProvider = new MockCanvasProvider();
        bean.generateBarcode(canvasProvider, "a");
        assertEquals(canvasProvider.fill, 222);
        assertEquals(canvasProvider.fillwhite, 0);
    }

    @Test
    public void testGenerateBarcodeSwissCross() {
        QRCodeBean bean = new QRCodeBean();
        bean.swissCross = true;
        MockCanvasProvider canvasProvider = new MockCanvasProvider();
        bean.generateBarcode(canvasProvider, "a");
        assertEquals(canvasProvider.fill, 199);
        assertEquals(canvasProvider.fillwhite, 2);
    }

    static class MockCanvasProvider implements CanvasProvider {
        int fill;
        int fillwhite;

        public void establishDimensions(BarcodeDimension dim) {
        }

        public BarcodeDimension getDimensions() {
            return null;
        }

        public int getOrientation() {
            return 0;
        }

        public void deviceFillRect(double x, double y, double w, double h) {
            fill++;
        }

        public void deviceFillRectWhite(double x, double y, double w, double h) {
            fillwhite++;
        }

        public void deviceJustifiedText(String text, double x1, double x2, double y1, String fontName, double fontSize) {
        }

        public void deviceCenteredText(String text, double x1, double x2, double y1, String fontName, double fontSize) {
        }

        public void deviceText(String text, double x1, double x2, double y1, String fontName, double fontSize,
                               TextAlignment textAlign) {
        }
    }
}
