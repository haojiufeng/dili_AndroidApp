package com.diligroup.bean;

/**
 * Created by hjf on 2016/8/30.
 */
public class AfterNoFoodBean extends CommonBean {
    /**
     * id : 1
     * nutrientContent : 我们为您推荐的食谱营养均衡，可以满足您一天的营养素需求。如果您的摄入量没有达到我们的推荐量，您可以适当加餐，以保证充足的营养摄入。如果您的摄入量超过推荐量，您需要通过增加运动来消耗多余的能量。
     * structureContent : 我们为您推荐的食谱食物种类丰富，搭配合理，肉类适量，蔬菜充足，符合中国居民膳食指南推荐的“食物多样、谷类为主”的平衡膳食模式。您可以根据个人喜好，适当增加水果的摄入，以保证每天摄入食物的丰富多样性。
     */

    private DataNomealsBean dataNomeals;

    public DataNomealsBean getDataNomeals() {
        return dataNomeals;
    }

    public void setDataNomeals(DataNomealsBean dataNomeals) {
        this.dataNomeals = dataNomeals;
    }

    public static class DataNomealsBean {
        private int id;
        private String nutrientContent;
        private String structureContent;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNutrientContent() {
            return nutrientContent;
        }

        public void setNutrientContent(String nutrientContent) {
            this.nutrientContent = nutrientContent;
        }

        public String getStructureContent() {
            return structureContent;
        }

        public void setStructureContent(String structureContent) {
            this.structureContent = structureContent;
        }
    }
}
