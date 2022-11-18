import React from 'react';
import Paginate, { ReactPaginateProps } from 'react-paginate';
import { Wrapper } from './style';

// interface pageInfoType {
// 	page: number;
// 	size: number;
// 	totalElements: string[];
// 	totalPages: number;
// }

export type OnPageChangeCallback = ReactPaginateProps['onPageChange'];

interface Props {
	currentPage: number;
	pageCount: number;
	onPageChange?: OnPageChangeCallback;
}

const Pagination = ({ currentPage, pageCount, onPageChange }: Props) => {
	return (
		<Wrapper>
			<Paginate
				forcePage={currentPage}
				pageCount={pageCount}
				marginPagesDisplayed={1}
				pageRangeDisplayed={1}
				onPageChange={onPageChange}
				nextLabel="&gt;"
				previousLabel="&lt;"
			/>
		</Wrapper>
	);
};

export default Pagination;
