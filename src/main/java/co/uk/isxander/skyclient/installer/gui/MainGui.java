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

        Image icon = FileUtils.getResourceImage("/skyclient.png");

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
            if (pack.getActions().length > 0) {
                actionButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        genPopup(pack.getActions()).show(e.getComponent(), e.getX(), e.getY());
                    }
                });
            } else {
                actionButton.setOpaque(false);
                actionButton.setContentAreaFilled(false);
                actionButton.setBorderPainted(false);
                actionButton.setText("");
                actionButton.setEnabled(false);
            }

            constraints.gridx = 2;
            constraints.gridy = i;
            gridBag.setConstraints(actionButton, constraints);
            packPane.add(actionButton);

            i++;
        }

        JButton installButton = new JButton("Install");
        installButton.addActionListener((action) -> {
            sc.install();
        });
        installButton.setPreferredSize(new Dimension(200, 30));
        constraints.insets = new Insets(1, 1, 1, 3);
        constraints.gridwidth = 4;
        constraints.gridx = 0;
        constraints.gridy = 2;
        gridBag.setConstraints(installButton, constraints);
        container.add(installButton);

        JLabel pathDisplayLabel = new JLabel(sc.getScDir().getAbsolutePath());
        pathDisplayLabel.setPreferredSize(new Dimension(150, 30));
        constraints.insets = new Insets(1, 1, 1, 3);
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 3;
        gridBag.setConstraints(pathDisplayLabel, constraints);
        container.add(pathDisplayLabel);

        JButton pathButton = new JButton("Select Path");
        pathButton.addActionListener((action) -> {
            JFrame pathFrame = new JFrame("Select Path");
            pathFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            pathFrame.setIconImage(FileUtils.getResourceImage("/skyclient.png"));
            pathFrame.setResizable(false);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addActionListener((listener) -> {
                if (listener.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    sc.setMcDir(fileChooser.getSelectedFile());
                    pathDisplayLabel.setText(sc.getMcDir().getAbsolutePath());
                } else if (listener.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
                    pathFrame.dispose();
                }
            });
            fileChooser.setCurrentDirectory(sc.getMcDir());
            fileChooser.setDialogTitle("Select Minecraft Data Folder");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);

            pathFrame.add(fileChooser);
            pathFrame.pack();
            pathFrame.setVisible(true);
        });
        pathButton.setPreferredSize(new Dimension(50, 30));
        constraints.insets = new Insets(1, 1, 1, 3);
        constraints.gridwidth = 1;
        constraints.gridx = 3;
        constraints.gridy = 3;
        gridBag.setConstraints(pathButton, constraints);
        container.add(pathButton);

        JLabel modLabel = new JLabel("Mods", SwingConstants.CENTER);
        modLabel.setPreferredSize(new Dimension(200, 30));
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        gridBag.setConstraints(modLabel, constraints);
        container.add(modLabel);

        JScrollPane modScrollPane = new JScrollPane(modPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        modScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        modScrollPane.setPreferredSize(new Dimension(370, 500));
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 1;
        gridBag.setConstraints(modScrollPane, constraints);
        container.add(modScrollPane);

        JLabel packLabel = new JLabel("Packs", SwingConstants.CENTER);
        packLabel.setPreferredSize(new Dimension(200, 30));
        constraints.gridwidth = 2;
        constraints.gridx = 2;
        constraints.gridy = 0;
        gridBag.setConstraints(packLabel, constraints);
        container.add(packLabel);

        JScrollPane packScrollPane = new JScrollPane(packPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        packScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        packScrollPane.setPreferredSize(new Dimension(370, 500));
        constraints.gridwidth = 2;
        constraints.gridx = 2;
        constraints.gridy = 1;
        gridBag.setConstraints(packScrollPane, constraints);
        container.add(packScrollPane);

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
