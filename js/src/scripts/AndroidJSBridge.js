const NAME = "AndroidJS";

export default class AndroidJSBridge {

    static mapReady() {
        eval(`${NAME}.mapReady()`);
    }

    static getFeatures(layerName, xmin, ymin, xmax, ymax) {
        eval(`${NAME}.getFeatures('${layerName}',${xmin},${ymin},${xmax},${ymax})`);
    }

    static getTile(x, y, z) {
        eval(`${NAME}.getTile(${x},${y},${z})`);
    }
}