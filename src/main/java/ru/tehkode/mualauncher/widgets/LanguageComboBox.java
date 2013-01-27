/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.mualauncher.widgets;

import java.util.Locale;
import java.util.Set;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import ru.tehkode.mualauncher.utils.Resources;
import ru.tehkode.mualauncher.widgets.LanguageComboBox.LanguageItem;

/**
 *
 * @author t3hk0d3
 */
public class LanguageComboBox extends JComboBox<LanguageItem> {

    public LanguageComboBox() {
        super(loadLocales());
    }

    @Override
    public void setModel(ComboBoxModel<LanguageItem> aModel) {
        if (aModel == null) {
            return;
        }

        super.setModel(aModel);
    }

    private static ComboBoxModel<LanguageItem> loadLocales() {
        Set<Locale> locales = Resources.getInstance().getAvailableLocales();
        LanguageItem items[] = new LanguageItem[locales.size()];

        int i = 0;
        for (Locale locale : locales) {
            items[i++] = new LanguageItem(locale);
        }

        return new DefaultComboBoxModel<LanguageItem>(items);
    }
    
    public void setSelectedLocale(Locale locale) {
        this.setSelectedItem(new LanguageItem(locale));
    }

    public Locale getSelectedLocale() {
        LanguageItem item = (LanguageItem) this.getSelectedItem();

        if (item == null) {
            return null;
        }

        return item.getLocale();
    }

    protected static class LanguageItem {

        private final Locale locale;

        public LanguageItem(Locale locale) {
            this.locale = locale;
        }

        public Locale getLocale() {
            return locale;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 41 * hash + (this.locale != null ? this.locale.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LanguageItem other = (LanguageItem) obj;
            if (this.locale != other.locale && (this.locale == null || !this.locale.equals(other.locale))) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString() {
            return locale.getDisplayLanguage(locale);
        }
    }
}
