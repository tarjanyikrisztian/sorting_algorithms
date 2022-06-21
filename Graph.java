package sorting_algorithms;

import com.formdev.flatlaf.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Graph extends javax.swing.JFrame {

    private final int WIDTH = 1000, HEIGHT = WIDTH * 9 / 16;
    private int SIZE = 200;
    private String Sort_Name = "Bubble";
    private float BAR_WIDTH = (float) WIDTH / SIZE;
    private float[] bar_height = new float[SIZE];
    private int current_index, traversing_index;
    private SwingWorker sorter;
    private boolean done = false, started = false;
    private int done_index = 0;
    private LookAndFeel lf_light = new FlatLightLaf();
    private LookAndFeel lf_dark = new FlatDarkLaf();
    private boolean dark_mode = false;
    private ImageIcon dark_icon = new ImageIcon(getClass().getResource("/sorting_algorithms/dark.png"));
    private ImageIcon light_icon = new ImageIcon(getClass().getResource("/sorting_algorithms/light.png"));
    private int speed = (202-SIZE)/2;

    public Graph() {
        initComponents();
    }

    public void ended() {
        started = false;
        jButton1.setEnabled(true);
        jSlider1.setEnabled(true);
        jComboBox1.setEnabled(true);
    }

    public void update() {
        done = false;
        done_index = 0;
        started = false;
        BAR_WIDTH = (float) WIDTH / SIZE;
        initBarHeight();
        Shuffle();
        revalidate();
        repaint();
    }

    public void Shuffle() {
        for (int i = 0; i < SIZE; i++) {
            Integer index = (int) (Math.random() * SIZE);
            Float temp = bar_height[i];
            bar_height[i] = bar_height[index];
            bar_height[index] = temp;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        Rectangle2D.Float bar;
        if (!done) {
            if (dark_mode) {
                g2d.setColor(new Color(51, 255, 255));
            } else {
                g2d.setColor(new Color(20, 204, 204));
            }
            for (int i = 0; i < SIZE; i++) {
                bar
                        = new Rectangle2D.Float(i * BAR_WIDTH, 31, BAR_WIDTH, bar_height[i]);
                g2d.fill(bar);
            }
            if (started) {
                g2d.setColor(Color.RED);
                bar = new Rectangle2D.Float(traversing_index * BAR_WIDTH, 31, BAR_WIDTH,
                        bar_height[traversing_index]);
                g2d.fill(bar);
                g2d.setColor(Color.GREEN);
                bar = new Rectangle2D.Float(current_index * BAR_WIDTH, 31, BAR_WIDTH,
                        bar_height[current_index]);
                g2d.fill(bar);
            }
        } else {
            if (dark_mode) {
                g2d.setColor(new Color(51, 51, 255));
            } else {
                g2d.setColor(new Color(20, 20, 204));
            }
            for (int i = 0; i < done_index; i++) {
                bar
                        = new Rectangle2D.Float(i * BAR_WIDTH, 31, BAR_WIDTH, bar_height[i]);
                g2d.fill(bar);
            }
            if (dark_mode) {
                g2d.setColor(new Color(51, 255, 255));
            } else {
                g2d.setColor(new Color(20, 204, 204));
            }
            for (int i = done_index; i < SIZE; i++) {
                bar
                        = new Rectangle2D.Float(i * BAR_WIDTH, 31, BAR_WIDTH, bar_height[i]);
                g2d.fill(bar);
            }
        }
        if (SIZE == done_index) {
            ended();
        }
    }

    private void initBarHeight() {
        float interval = (float) 420 / SIZE;
        for (int i = 0; i < SIZE; i++) {
            bar_height[i] = (i + 1) * interval;
        }
    }

    public void swap(int indexA, int indexB) {
        float temp = bar_height[indexA];
        bar_height[indexA] = bar_height[indexB];
        bar_height[indexB] = temp;
    }

    public void Bubble() {
        started = true;
        sorter = new SwingWorker() {
            public void bubbleSort() {
                for (int i = 0; i < SIZE - 1; i++) {
                    for (int j = 0; j < SIZE - i - 1; j++) {
                        if (bar_height[j] > bar_height[j + 1]) {
                            traversing_index = j;
                            current_index = j + 1;
                            swap(j, j + 1);
                            try {
                                Thread.sleep(2*speed);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Graph.class.getName())
                                        .log(Level.SEVERE, null, ex);
                            }
                            repaint();
                        }
                    }
                }
            }

            public void end() {
                current_index = 0;
                traversing_index = 0;
                done = true;
                while (done_index < SIZE) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    repaint();
                    done_index++;
                }
            }

            @Override
            public Void doInBackground() {
                bubbleSort();
                end();

                return null;
            }
        };
        sorter.execute();
    }

    public void Selection() {
        started = true;
        sorter = new SwingWorker() {
            public void selectionSort() {
                for (int step = 0; step < SIZE - 1; step++) {
                    int min_idx = step;
                    for (int i = step + 1; i < SIZE; i++) {
                        if (bar_height[i] < bar_height[min_idx]) {
                            min_idx = i;
                        }
                    }
                    traversing_index = min_idx;
                    current_index = step;
                    swap(traversing_index, current_index);
                    repaint();
                    try {
                        Thread.sleep(10*speed);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            public void end() {
                current_index = 0;
                traversing_index = 0;
                done = true;
                while (done_index < SIZE) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    repaint();
                    done_index++;
                }
            }

            @Override
            public Void doInBackground() {
                selectionSort();
                end();
                return null;
            }
        };
        sorter.execute();

    }

    public void Insertion() {
        started = true;
        sorter = new SwingWorker() {
            public void insertionSort() {
                for (int step = 1; step < SIZE; step++) {
                    float key = bar_height[step];
                    current_index = step;
                    int j = step - 1;
                    while (j >= 0 && key < bar_height[j]) {
                        bar_height[j + 1] = bar_height[j];
                        --j;
                    }
                    traversing_index = j + 1;

                    bar_height[j + 1] = key;
                    repaint();
                    try {
                        Thread.sleep(10*speed);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }

            public void end() {
                current_index = 0;
                traversing_index = 0;
                done = true;
                while (done_index < SIZE) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    repaint();
                    done_index++;
                }
            }

            @Override
            public Void doInBackground() {
                insertionSort();
                end();
                return null;
            }
        };
        sorter.execute();
    }

    public void Quick() {
        started = true;
        sorter = new SwingWorker() {
            public int partition(int lowIndex, int highIndex) {
                int pivot = highIndex;
                int i = lowIndex - 1;
                for (int j = lowIndex; j < highIndex; j++) {
                    if (bar_height[pivot] > bar_height[j]) {
                        i++;
                        current_index = i;
                        traversing_index = j;
                        swap(i, j);
                        try {
                            Thread.sleep(10*speed);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Graph.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }

                        repaint();
                    }
                }
                current_index = i + 1;
                traversing_index = pivot;
                swap(i + 1, pivot);
                try {
                    Thread.sleep(10*speed);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                }
                repaint();
                return i + 1;
            }

            public void quickSort(int lowIndex, int highIndex) {
                if (lowIndex < highIndex) {
                    int i = partition(lowIndex, highIndex);
                    try {
                        quickSort(lowIndex, i - 1);
                    } finally {
                        quickSort(i + 1, highIndex);
                    }
                }
            }

            public void end() {
                current_index = 0;
                traversing_index = 0;
                done = true;
                while (done_index < SIZE) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    repaint();
                    done_index++;
                }
            }

            @Override
            public Void doInBackground() {
                quickSort(0, SIZE);
                end();
                return null;
            }
        };
        sorter.execute();
    }

    public void Merge() {
        started = true;
        sorter = new SwingWorker() {
            public void mergeSort(int left, int right) {
                if (right <= left) {
                    return;
                }
                int mid = (left + right) / 2;
                mergeSort(left, mid);
                mergeSort(mid + 1, right);
                mergeing(left, mid, right);
            }

            public void mergeing(int left, int mid, int right) {
                int lengthLeft = mid - left + 1;
                int lengthRight = right - mid;

                float leftArray[] = new float[lengthLeft];
                float rightArray[] = new float[lengthRight];

                for (int i = 0; i < lengthLeft; i++) {
                    leftArray[i] = bar_height[left + i];
                }
                for (int i = 0; i < lengthRight; i++) {
                    rightArray[i] = bar_height[mid + i + 1];
                }

                int leftIndex = 0;
                int rightIndex = 0;
                current_index = mid;
                for (int i = left; i < right + 1; i++) {
                    if (leftIndex < lengthLeft && rightIndex < lengthRight) {
                        if (leftArray[leftIndex] < rightArray[rightIndex]) {
                            bar_height[i] = leftArray[leftIndex];
                            traversing_index = leftIndex;
                            leftIndex++;
                        } else {
                            bar_height[i] = rightArray[rightIndex];
                            traversing_index = rightIndex;
                            rightIndex++;
                        }
                    } else if (leftIndex < lengthLeft) {
                        bar_height[i] = leftArray[leftIndex];
                        traversing_index = leftIndex;
                        leftIndex++;
                    } else if (rightIndex < lengthRight) {
                        bar_height[i] = rightArray[rightIndex];
                        traversing_index = rightIndex;
                        rightIndex++;
                    }
                    try {
                        Thread.sleep(10*speed);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    repaint();
                }
            }

            public void end() {
                current_index = 0;
                traversing_index = 0;
                done = true;
                while (done_index < SIZE) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    repaint();
                    done_index++;
                }
            }

            @Override
            public Void doInBackground() {
                mergeSort(0, SIZE);
                end();
                return null;
            }
        };
        sorter.execute();

    }

    public void Heap() {
        started = true;
        sorter = new SwingWorker() {
            void heapify(int length, int i) {
                int leftChild = 2 * i + 1;
                int rightChild = 2 * i + 2;
                int largest = i;

                if (leftChild < length && bar_height[leftChild] > bar_height[largest]) {
                    largest = leftChild;
                }

                if (rightChild < length
                        && bar_height[rightChild] > bar_height[largest]) {
                    largest = rightChild;
                }

                if (largest != i) {
                    current_index = largest;
                    traversing_index = i;
                    swap(traversing_index, current_index);
                    try {
                        Thread.sleep(10*speed);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    repaint();
                    heapify(length, largest);
                }
            }

            public void heapSort() {
                for (int i = SIZE / 2 - 1; i >= 0; i--) {
                    heapify(SIZE, i);
                }

                for (int i = SIZE - 1; i >= 0; i--) {
                    current_index = i;
                    traversing_index = 0;
                    swap(traversing_index, current_index);
                    try {
                        Thread.sleep(10*speed);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    repaint();

                    heapify(i, 0);
                }
            }

            public void end() {
                current_index = 0;
                traversing_index = 0;
                done = true;
                while (done_index < SIZE) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    repaint();
                    done_index++;
                }
            }

            @Override
            public Void doInBackground() {
                heapSort();
                end();
                return null;
            }
        };
        sorter.execute();

    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        Image image = dark_icon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        dark_icon = new ImageIcon(newimg);
        image = light_icon.getImage(); // transform it 
        newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        light_icon = new ImageIcon(newimg);// transform it back

        update();
        jSlider1 = new javax.swing.JSlider();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jSlider1.setMaximum(200);
        jSlider1.setMinimum(10);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSlider1.setValue(200);

        jSlider1.addChangeListener((javax.swing.event.ChangeEvent evt) -> {
            jSlider1StateChanged(evt);
        });

        jButton1.setLabel("Visualize Bubble sort");
        jButton1.addActionListener((java.awt.event.ActionEvent evt) -> {
            jButton1ActionPerformed(evt);
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Bubble", "Selection", "Insertion", "Quick", "Merge", "Heap"}));
        jComboBox1.addActionListener((java.awt.event.ActionEvent evt) -> {
            jComboBox1ActionPerformed(evt);
        });

        jButton2.setIcon(dark_icon);
        //jButton2.setOpaque(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setBorderPainted(false);
        jButton2.addActionListener((java.awt.event.ActionEvent evt) -> {
            jToggleButton1ActionPerformed(evt);
        });

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        javax.swing.GroupLayout layout
                = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                layout.createSequentialGroup()
                                        .addGap(305, 305, 305)
                                        .addComponent(jButton2,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(3, 3, 3)
                                        .addComponent(jSlider1,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 334,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(8, 8, 8)
                                        .addComponent(jComboBox1,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 75,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(250, 250, 250)
                        )
                        .addGroup(layout.createSequentialGroup().addGroup(
                                layout
                                        .createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                layout.createSequentialGroup()
                                                        .addGap(420, 420, 420)
                                                        .addComponent(jButton1,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 160,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(420, 420, 420))
                        )));

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                layout.createSequentialGroup()
                                        .addGap(480, 480, 480)
                                        .addComponent(jComboBox1,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                layout.createSequentialGroup()
                                        .addGap(490, 490, 490)
                                        .addComponent(jButton2,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                25,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                layout.createSequentialGroup()
                                        .addGap(460, 460, 460)
                                        .addComponent(jButton1)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSlider1,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        ));

        pack();
    }

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {
        SIZE = jSlider1.getValue();
        update();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        jButton1.setEnabled(false);
        jSlider1.setEnabled(false);
        jComboBox1.setEnabled(false);
        switch (Sort_Name) {
            case "Bubble" ->
                Bubble();
            case "Selection" ->
                Selection();
            case "Insertion" ->
                Insertion();
            case "Quick" ->
                Quick();
            case "Merge" ->
                Merge();
            case "Heap" ->
                Heap();
            default ->
                throw new AssertionError();
        }

    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        Sort_Name = jComboBox1.getSelectedItem().toString();
        jButton1.setLabel("Visualize " + Sort_Name + " Sort");
        update();
    }

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {

        dark_mode = !dark_mode;
        if (dark_mode) {
            jButton2.setIcon(light_icon);
        } else {
            jButton2.setIcon(dark_icon);
        }
        change_lf(this);
    }

    private void change_lf(JFrame frame) {
        try {
            if (dark_mode) {
                UIManager.setLookAndFeel(lf_dark);
            } else {
                UIManager.setLookAndFeel(lf_light);
            }
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public void Visualize() {
        try {
            UIManager.setLookAndFeel(lf_light);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> {
            JFrame frame = new Graph();
            frame.setTitle("Sorting Algorithm Visulaizer");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.revalidate();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

    }

    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JButton jButton2;
}
