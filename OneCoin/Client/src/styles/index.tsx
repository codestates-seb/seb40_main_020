import { css } from 'styled-components';

export const flexCenter = css`
	display: flex;
	align-items: center;
	justify-content: center;
`;

export const flexRowBetween = css`
	display: flex;
	align-items: center;
	justify-content: space-between;
`;

export const flexColumnCenter = css`
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
`;

export const flexRowCenter = css`
	display: flex;
	align-items: center;
`;

export const flexColumn = css`
	display: flex;
	flex-direction: column;
`;

export const container = css`
	max-width: 1400px;
	width: 100%;
	display: flex;
	margin: 0 auto;
`;
