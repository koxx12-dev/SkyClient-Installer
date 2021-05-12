package co.uk.isxander.skyclient.installer.gui;

import co.uk.isxander.skyclient.installer.SkyClient;
import co.uk.isxander.skyclient.installer.repo.entry.EntryAction;
import co.uk.isxander.skyclient.installer.repo.entry.EntryWarning;
import co.uk.isxander.skyclient.installer.repo.entry.ModEntry;
import co.uk.isxander.skyclient.installer.repo.entry.PackEntry;
import co.uk.isxander.skyclient.installer.utils.FileUtils;
import co.uk.isxander.skyclient.installer.utils.ImageUtils;
import co.uk.isxander.skyclient.installer.utils.UpdateHook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class MainGui {

    private final SkyClient sc;

    private final Map<ModEntry, JLabel> modIcons;
    private final Map<PackEntry, JLabel> packIcons;

    public MainGui(SkyClient sc) {
        this.sc = sc;
        this.modIcons = new HashMap<>();
        this.packIcons = new HashMap<>();

        sc.getRepositoryManager().fetchFiles(new UpdateHook() {
            @Override
            public void updateMod(ModEntry mod) {
                refreshModIcon(mod);
            }

            @Override
            public void updatePack(PackEntry pack) {
                refreshPackIcon(pack);
            }
        });

        Image icon = FileUtils.getResourceImage("skyclient.png");

        JFrame frame = new JFrame("SkyClient Installer (Java Edition)");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImage(icon);
        frame.setResizable(false);

        Container container = frame.getContentPane();

        JPanel modPane = new JPanel();
        JPanel packPane = new JPanel();

        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        container.setLayout(gridBag);
        modPane.setLayout(gridBag);
        packPane.setLayout(gridBag);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        int i = 0;
        for (ModEntry mod : sc.getRepositoryManager().getModEntries()) {
            if (mod.isHidden()) {
                continue;
            }

            JLabel imgLabel = new JLabel(new ImageIcon(ImageUtils.getScaledImage(sc.getRepositoryManager().getImage(mod.getIconFile()), 50, 50)));
            imgLabel.setName(mod.getId());
            imgLabel.setPreferredSize(new Dimension(50, 50));
            constraints.insets = new Insets(1, 1, 1, 1);
            constraints.gridx = 0;
            constraints.gridy = i;
            gridBag.setConstraints(imgLabel, constraints);
            modIcons.put(mod, imgLabel);
            modPane.add(imgLabel);

            JCheckBox checkBox = new JCheckBox(mod.getDisplayName());
            checkBox.setName(mod.getId());
            checkBox.setSelected(mod.isEnabled());
            checkBox.addActionListener((action) -> {
                if (warn(mod.getWarning())) {
                    mod.setEnabled(checkBox.isSelected());
                }
            });
            constraints.gridx = 1;
            constraints.gridy = i;
            gridBag.setConstraints(checkBox, constraints);
            modPane.add(checkBox);

            JButton actionButton = new JButton("^");
            actionButton.setName(mod.getId());
            actionButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    genPopup(mod.getActions()).show(e.getComponent(), e.getX(), e.getY());
                }
            });
            constraints.gridx = 2;
            constraints.gridy = i;
            gridBag.setConstraints(actionButton, constraints);
            modPane.add(actionButton);

            i++;
        }

        i = 0;
        for (PackEntry pack : sc.getRepositoryManager().getPackEntries()) {
            if (pack.isHidden()) {
                continue;
            }

            JLabel imgLabel = new JLabel(new ImageIcon(ImageUtils.getScaledImage(sc.getRepositoryManager().getImage(pack.getIconFile()), 50, 50)));
            imgLabel.setName(pack.getId());
            imgLabel.setPreferredSize(new Dimension(50, 50));
            constraints.insets = new Insets(1, 1, 1, 1);
            constraints.gridx = 0;
            constraints.gridy = i;
            gridBag.setConstraints(imgLabel, constraints);
            packIcons.put(pack, imgLabel);
            packPane.add(imgLabel);

            JCheckBox checkBox = new JCheckBox(pack.getDisplayName());
            checkBox.setName(pack.getId());
            checkBox.setSelected(pack.isEnabled());
            checkBox.addActionListener((action) -> {
                if (warn(pack.getWarning())) {
                    pack.setEnabled(checkBox.isSelected());
                }
            });
            constraints.gridx = 1;
            constraints.gridy = i;
            gridBag.setConstraints(checkBox, constraints);
            packPane.add(checkBox);

            JButton actionButton = new JButton("^");
            actionButton.setName(pack.getId());
            actionButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    genPopup(pack.getActions()).show(e.getComponent(), e.getX(), e.getY());
                }
            });
            constraints.gridx = 2;
            constraints.gridy = i;
            gridBag.setConstraints(actionButton, constraints);
            packPane.add(actionButton);

            i++;
        }

        frame.pack();
        frame.setVisible(true);
    }

    private static boolean warn(EntryWarning warning) {
        if (warning == null)
            return true;

        int option = JOptionPane.showConfirmDialog(null, warning.getMessageHtml().replaceAll("\"", ""), "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return option == JOptionPane.YES_OPTION;
    }

    private static JPopupMenu genPopup(EntryAction[] actions){
        JPopupMenu popup = new JPopupMenu();

        for (EntryAction action : actions) {
            JMenuItem menuItem = new JMenuItem(action.getDisplay());
            menuItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(new URI(action.getUrl()));
                        } else {
                            SkyClient.LOGGER.severe("Computer does not appear to support browsing.");
                        }
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            popup.add(menuItem);
        }

        popup.setSize(popup.getWidth() * 4, popup.getHeight() * 4);
        return popup;
    }

    public void refreshModIcon(ModEntry mod) {
        modIcons.get(mod).setIcon(new ImageIcon(ImageUtils.getScaledImage(sc.getRepositoryManager().getImage(mod.getIconFile()), 50, 50)));
    }

    public void refreshPackIcon(PackEntry pack) {
        packIcons.get(pack).setIcon(new ImageIcon(ImageUtils.getScaledImage(sc.getRepositoryManager().getImage(pack.getIconFile()), 50, 50)));
    }

}
