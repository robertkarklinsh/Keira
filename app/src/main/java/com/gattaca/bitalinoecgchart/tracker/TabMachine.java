package com.gattaca.bitalinoecgchart.tracker;

import com.gattaca.bitalinoecgchart.tracker.data.DrugItemContainer;
import com.gattaca.bitalinoecgchart.tracker.data.ProgressBarItemContainer;
import com.gattaca.bitalinoecgchart.tracker.data.TaskItemContainer;
import com.gattaca.bitalinoecgchart.tracker.data.TrackerItemContainer;
import com.gattaca.bitalinoecgchart.tracker.data.ViewType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 26.08.2016.
 */
public class TabMachine {



    static ExpandHelper expandHelper = new ExpandHelper();

    static public int getHeaderPos( Boolean[] expanded, int pos) {
        return expandHelper.getHeaderPos(expanded, pos);
    }

    static public int getViewType(int pos, Boolean[] expanded) {
        return expandHelper.getViewType(expanded, pos).number;
    }

    static public int getExpandPosition(int positionInList, Boolean[] expanded) {

        return expandHelper.getExpandPosition(expanded, positionInList);
    }

    static public TrackerItemContainer getItemContainer(int position, Boolean[] expanded) {
        return expandHelper.getItemContainer(expanded, position);

    }

    static public String getHeaderName(int position, Boolean[] expanded) {

        return expandHelper.getHeaderName(expanded, position);
    }

    static public int expandCount(Boolean[] expanded, int position) {
        return expandHelper.expandCount(expanded, position);
    }

    static public int getStartFromPos(Boolean[] expanded, int position) {
        return expandHelper.getStartFromPos(expanded, position);
    }

    static public int getAllCount(Boolean[] expanded) {
        return expandHelper.getAllCount(expanded);
    }

    static class ExpandHelper {
        private List<ViewType> list = new ArrayList<>();
        int counts[] = {4, 3, 2};
        int collapseCount = 2;

        ExpandHelper() {
//             list
        }

        public int getHeaderPos(Boolean[] expanded, int position) {
          populateList(expanded);
            for(int i = position ; i > 0; i --)
            {
                if (list.get(i) == ViewType.HEADER) {
                    return i;
                }
            }
            return 0;
        }

        public  ViewType getViewType(Boolean[] expanded, int position) {
            populateList(expanded);
            return list.get(position);
        }

        public int getAllCount(Boolean[] expanded) {
            populateList(expanded);
            return list.size();
        }

        private void populateList(Boolean[] expanded) {
            list.clear();
            for (int i = 0; i < 3; i++) {
                list.add(ViewType.HEADER);
                int amt = expanded[i] ? counts[i] : collapseCount;

                for (int j = 0; j < amt; j++) {
                    list.add(ViewType.ITEM);
                }
                list.add(ViewType.FOOTER);

            }
        }

        public int expandCount(Boolean[] expanded, int position) {
            populateList(expanded);
            int lastZero = 0;
            int res = 0;
            for (int i = 0; i < position; i++) {
                if (list.get(i) == ViewType.HEADER) {
                    lastZero = i;
                    res++;
                }
            }
            return counts[res - 1] - 2;

        }

        public int getStartFromPos(Boolean[] expanded, int position) {
            populateList(expanded);
            int lastZero = 0;
            for (int i = 0; i < position; i++) {
                if (list.get(i) == ViewType.HEADER) {
                    lastZero = i;
                }
            }
            return lastZero + 2;

        }

        public TrackerItemContainer getItemContainer(Boolean[] expanded, int position) {
            populateList(expanded);
            int res = 0;
            for (int i = 0; i < position; i++) {
                if (list.get(i) == ViewType.HEADER) {
                    res++;
                }
            }
            if (res == 1) {
                return DrugItemContainer.example(position);
            } else if (res == 2) {
                return ProgressBarItemContainer.example(position);
            } else {
                return TaskItemContainer.example(position);
            }

        }

        public String getHeaderName(Boolean[] expanded, int position) {
            populateList(expanded);
            int res = 0;
            for (int i = 0; i <= position; i++) {
                if (list.get(i) == ViewType.HEADER) {
                    res++;
                }
            }
            if (res == 1) {
                return "Лекарства";
            } else if (res == 2) {
                return "Измерения";
            } else {
                return "Задания";
            }
        }

        public int getExpandPosition(Boolean[] expanded, int position) {
            populateList(expanded);
            int res = 0;
            for (int i = 0; i < position; i++) {
                if (list.get(i) == ViewType.HEADER) {
                    res++;
                }
            }
            return res - 1;
        }

    }

}
