import styled from 'styled-components';

export const ToggleComponent = styled.div`
	width: 60px;
	height: 60px;
	border-radius: 50%;
	position: fixed;
	right: 30px;
	bottom: 35px;
	display: flex;
	cursor: pointer;
	justify-content: center;
	align-items: center;
	font-size: 2.5rem;
	background: var(--yellow);
	svg {
		font-size: 1.8rem;
	}
`;
