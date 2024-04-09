import { Style, Fill, Stroke, Circle } from 'ol/style';

export default {
    RAILWAY: [
        new Style({
            stroke: new Stroke({ color: 'white', width: 5 }),
        }),
        new Style({
            stroke: new Stroke({ color: 'black', width: 4, lineDash: [9, 9] }),
        })
    ],
    TAXI_STANDS: new Style({
        image: new Circle({
            radius: 6,
            fill: new Fill({ color: '#ffea00' }),
            stroke: new Stroke({ color: "black", width: 2 })
        })
    })
};
