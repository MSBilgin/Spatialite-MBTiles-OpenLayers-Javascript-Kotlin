import Map from 'ol/Map';
import View from 'ol/View';
import { defaults as defaultControls } from 'ol/control.js';
import { defaults as defaultInteractions } from 'ol/interaction.js';
import 'ol/ol.css';
import '../styles/index.css';

import AndroidJSBridge from './AndroidJSBridge';
import Utils from './util/Utils';

window.start = (x, y, z) => {
    const map = new Map({
        target: 'map',
        moveTolerance: 7,
        layers: [],
        controls: defaultControls({ attribution: false, zoom: false, rotate: false }),
        interactions: defaultInteractions({ pinchRotate: false}),
        view: new View({ center: [x, y], zoom: z })
    });

     window.Utils = new Utils(map);
     AndroidJSBridge.mapReady();
};


