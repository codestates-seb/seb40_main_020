import React, { memo } from 'react';
import { ChartComponent } from './style';
import { AdvancedRealTimeChart } from 'react-ts-tradingview-widgets';

interface Props {
	symbol: string;
}

function Chart({ symbol }: Props) {
	return (
		<ChartComponent>
			<AdvancedRealTimeChart
				theme="light"
				autosize
				interval={'1'}
				timezone={'Asia/Seoul'}
				symbol={`UPBIT:${symbol}`}
			/>
		</ChartComponent>
	);
}

export default memo(Chart);
