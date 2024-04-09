import VectorSource from 'ol/source/Vector';
import VectorImageLayer from 'ol/layer/VectorImage';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { bbox as bboxStrategy } from 'ol/loadingstrategy';
import WKTFormat from 'ol/format/WKT';
import AndroidJSBridge from "../AndroidJSBridge";
import Styles from "../style/Styles";
import TileState from 'ol/TileState';

export default class Layer {

    constructor(map) {
        this.map = map;
    }

    addTileLayer(zoom) {
        let source = new XYZ({
            transition: 0,
            maxZoom: zoom,
            url: '{z}/{x}/{y}',
            tileLoadFunction: (tile) => {
                let [z, x, y] = tile.getTileCoord();
                AndroidJSBridge.getTile(x, y, z);
            }
        });

        let layer = new TileLayer({ source: source });
        layer.set('name', 'basemap');
        this.map.addLayer(layer);
    }

    addVectorLayer(layerName, styleName) {
        let source = new VectorSource({
            strategy: bboxStrategy,
            projection: 'EPSG:3857',
            useSpatialIndex: true,
            loader: (extent) => AndroidJSBridge.getFeatures(layerName, extent[0], extent[1], extent[2], extent[3])
        });

        let layer = new VectorImageLayer({
            source: source,
            style: Styles[styleName]
        });

        layer.set('name', layerName);
        this.map.addLayer(layer);
    }

    /**
     * Katman adından katmanın kendisini getirir.
     * @param {string} layerName 
     */
    #getLayer(layerName) {
        for (let i = 0; i < this.map.getLayers().getArray().length; i++) {
            if (this.map.getLayers().getArray()[i].get('name') == layerName) {
                return this.map.getLayers().getArray()[i];
            }
        }
        return null;
    }

    setEnabled(layerName, status) {
        this.#getLayer(layerName).setVisible(status);
    }

    async loadTileData(x, y, z, veri) {
        let tileSource = this.#getLayer("basemap").getSource();
        let tile = tileSource.getTile(z, x, y);
        tile.setState(TileState.LOADING);

        if (veri) {
            tile.getImage().src = veri;
            tile.setState(TileState.LOADED);
        } else {
            tile.setState(TileState.ERROR);
        }

        tileSource.changed();
    }

    async loadFeatureData(layerName, idWktArray) {
        let format = new WKTFormat();
        let source = this.#getLayer(layerName).getSource();
        
        idWktArray.forEach(idWkt => {
            let data = idWkt.split(';');
            let id = data[0];
            let wkt = data[1];
            let feature = format.readFeature(wkt);
            feature.setId(id);
            source.addFeature(feature);
        })
    }
}