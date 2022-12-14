import styled from 'styled-components';

interface Props {
	proceeds: number;
}

export const Wrapper = styled.div`
	padding: 20px 30px;
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(50%, auto));
	border-bottom: 1px solid var(--borderColor);
`;

export const MenuList = styled.ul<Props>`
	display: flex;
	:nth-child(4),
	:last-child {
		li:nth-child(2) {
			color: ${({ proceeds }) => (proceeds > 0 ? 'var(--red)' : 'var(--blue)')};
		}
	}
`;

export const MenuItem = styled.li`
	flex: 1;
	padding: 15px 0;
	:nth-child(2) {
		text-align: right;
		margin-right: 5px;
	}
`;
