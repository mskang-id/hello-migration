package com.shopmall.web;

import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.common.error.ErrorCode;
import com.shopmall.service.ReviewService;
import com.shopmall.web.dto.review.HelpfulReviewListXml;
import com.shopmall.web.dto.review.HelpfulReviewXml;
import com.shopmall.web.dto.review.HistogramBucketXml;
import com.shopmall.web.dto.review.RatingHistogramXml;
import com.shopmall.web.dto.review.ReviewListXml;
import com.shopmall.web.dto.review.ReviewRequestXml;
import com.shopmall.web.dto.review.ReviewSummaryXml;
import com.shopmall.web.dto.review.ReviewXml;
import com.shopmall.web.dto.review.VerifiedReviewListXml;
import com.shopmall.web.dto.review.VerifiedReviewXml;
import com.shopmall.web.dto.review.WeightedScoreXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired private ReviewService reviewService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse create(@RequestBody ReviewRequestXml req) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("productId", req.getProductId());
        param.put("memberId", req.getMemberId());
        param.put("rating", req.getRating());
        param.put("title", req.getTitle());
        param.put("body", req.getBody());

        long reviewId = reviewService.createReview(param);
        if (reviewId == -1L) {
            ReviewXml fail = new ReviewXml();
            fail.setReviewId(-1L);
            return ResponseFactory.fail(ErrorCode.NOT_FOUND, "invalid product or member", fail);
        }
        ReviewXml out = new ReviewXml();
        out.setReviewId(reviewId);
        out.setProductId(req.getProductId());
        out.setMemberId(req.getMemberId());
        out.setRating(req.getRating());
        out.setTitle(req.getTitle());
        out.setBody(req.getBody());
        return ResponseFactory.ok(out);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse listByProduct(@PathVariable("productId") long productId,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                     @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Map<String, Object> data = reviewService.listByProduct(productId, page, size);
        ReviewListXml out = new ReviewListXml();
        out.setTotal(((Number) data.get("total")).longValue());

        List<Map<String, Object>> rows = (List<Map<String, Object>>) data.get("rows");
        if (rows != null) {
            for (Map<String, Object> r : rows) {
                ReviewXml x = new ReviewXml();
                x.setReviewId(((Number) r.get("REVIEW_ID")).longValue());
                x.setProductId(((Number) r.get("PRODUCT_ID")).longValue());
                x.setMemberId(((Number) r.get("MEMBER_ID")).longValue());
                x.setRating(((Number) r.get("RATING")).intValue());
                x.setTitle(r.get("TITLE") == null ? null : String.valueOf(r.get("TITLE")));
                x.setBody(r.get("BODY") == null ? null : String.valueOf(r.get("BODY")));
                x.setRegDate(r.get("REG_DATE") == null ? null : String.valueOf(r.get("REG_DATE")));
                x.setMemberName(r.get("MEMBER_NAME") == null ? null : String.valueOf(r.get("MEMBER_NAME")));
                out.getRows().add(x);
            }
        }

        Map<String, Object> summary = (Map<String, Object>) data.get("summary");
        if (summary != null) {
            ReviewSummaryXml s = new ReviewSummaryXml();
            s.setProductId(((Number) summary.get("PRODUCT_ID")).longValue());
            s.setReviewCnt(((Number) summary.get("REVIEW_CNT")).longValue());
            // controller-tier math: round the AVG_RATING into a whole-number Long
            s.setAvgRating(Math.round(((Number) summary.get("AVG_RATING")).doubleValue()));
            s.setPositiveCnt(((Number) summary.get("POSITIVE_CNT")).longValue());
            out.setSummary(s);
        }
        return ResponseFactory.ok(out);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/product/{productId}/verified", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse verified(@PathVariable("productId") long productId,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Map<String, Object> data = reviewService.verifiedByProduct(productId, page, size);
        VerifiedReviewListXml out = new VerifiedReviewListXml();
        out.setTotal(((Number) data.get("total")).longValue());
        out.setVerifiedCount(((Number) data.get("verifiedCount")).longValue());
        List<Map<String, Object>> rows = (List<Map<String, Object>>) data.get("rows");
        if (rows != null) {
            for (Map<String, Object> r : rows) {
                VerifiedReviewXml x = new VerifiedReviewXml();
                x.setReviewId(((Number) r.get("REVIEW_ID")).longValue());
                x.setProductId(((Number) r.get("PRODUCT_ID")).longValue());
                x.setMemberId(((Number) r.get("MEMBER_ID")).longValue());
                x.setRating(((Number) r.get("RATING")).intValue());
                x.setTitle(r.get("TITLE") == null ? null : String.valueOf(r.get("TITLE")));
                x.setBody(r.get("BODY") == null ? null : String.valueOf(r.get("BODY")));
                x.setRegDate(r.get("REG_DATE") == null ? null : String.valueOf(r.get("REG_DATE")));
                x.setHelpfulCount(((Number) r.get("HELPFUL_COUNT")).longValue());
                x.setPurchaseState(r.get("PURCHASE_STATE") == null ? null : String.valueOf(r.get("PURCHASE_STATE")));   // EXISTS flag
                out.getRows().add(x);
            }
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/product/{productId}/weighted-score", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse weightedScore(@PathVariable("productId") long productId) {
        Map<String, Object> r = reviewService.weightedScore(productId);
        WeightedScoreXml out = new WeightedScoreXml();
        out.setProductId(productId);
        if (r != null) {
            out.setReviewCnt(((Number) r.get("REVIEW_CNT")).longValue());
            out.setWeightNum(((Number) r.get("WEIGHT_NUM")).longValue());
            out.setWeightDen(((Number) r.get("WEIGHT_DEN")).longValue());
            out.setWeightedScoreX100(((Number) r.get("WEIGHTED_SCORE_X100")).longValue());
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/product/{productId}/histogram", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse histogram(@PathVariable("productId") long productId) {
        List<Map<String, Object>> rows = reviewService.ratingHistogram(productId);
        RatingHistogramXml out = new RatingHistogramXml();
        out.setProductId(productId);
        if (rows != null) {
            for (Map<String, Object> r : rows) {
                HistogramBucketXml b = new HistogramBucketXml();
                b.setStar(((Number) r.get("STAR")).intValue());
                b.setCnt(((Number) r.get("CNT")).longValue());
                out.getBuckets().add(b);
            }
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/product/{productId}/top-helpful", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse topHelpful(@PathVariable("productId") long productId,
                                  @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        List<Map<String, Object>> rows = reviewService.topHelpful(productId, size);
        HelpfulReviewListXml out = new HelpfulReviewListXml();
        out.setProductId(productId);
        if (rows != null) {
            for (Map<String, Object> r : rows) {
                HelpfulReviewXml x = new HelpfulReviewXml();
                x.setReviewId(((Number) r.get("REVIEW_ID")).longValue());
                x.setProductId(((Number) r.get("PRODUCT_ID")).longValue());
                x.setMemberId(((Number) r.get("MEMBER_ID")).longValue());
                x.setRating(((Number) r.get("RATING")).intValue());
                x.setTitle(r.get("TITLE") == null ? null : String.valueOf(r.get("TITLE")));
                x.setHelpfulCount(((Number) r.get("HELPFUL_COUNT")).longValue());
                out.getRows().add(x);
            }
        }
        return ResponseFactory.ok(out);
    }
}
