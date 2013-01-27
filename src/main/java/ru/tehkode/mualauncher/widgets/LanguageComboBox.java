/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.mualauncher.widgets;

import java.util.Locale;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import ru.tehkode.mualauncher.utils.Resources;

/**
 *
 * @author t3hk0d3
 */
public class LanguageComboBox extends JComboBox<Locale> {

    public LanguageComboBox() {
        super(loadLocales());
    }

    @Override
    public void setModel(ComboBoxModel<Locale> aModel) {
        if (aModel == null) {
            return;
        }

        super.setModel(aModel);
    }

    private static ComboBoxModel<Locale> loadLocales() {
        return new DefaultComboBoxModel<Locale>(Resources.getInstance().getAvailableLocales().toArray(new Locale[0]));
    }

    protected class LanguageItem {

        private final Locale locale;

        public LanguageItem(Locale locale) {
            this.locale = locale;
        }

        public Locale getLocale() {
            return locale;
        }

        @Override
        public String toString() {
            return "LanguageItem{" + "locale=" + locale + '}';
        }
    }
}
